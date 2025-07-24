package com.ticket.terminal.service;

import com.ticket.terminal.dto.culture.PROCultureGUIDDto;
import com.ticket.terminal.dto.culture.PROCultureGUIDEntryDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.order.OrderServiceEntity;
import com.ticket.terminal.entity.ProCultureLinkEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.exception.ProCultureBindingException;
import com.ticket.terminal.repository.order.OrderServiceRepository;
import com.ticket.terminal.repository.ProCultureLinkRepository;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProCultureService {

    private final OrderServiceRepository orderServiceRepository;
    private final ProCultureLinkRepository proCultureLinkRepository;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    @Transactional
    public void saveProCultureGUIDs(PROCultureGUIDDto dto) {
        UsersEntity currentUser = getCurrentUser();
        dto.getSeat().forEach(entryDto -> handleSeatEntry(entryDto, currentUser));
    }

    private void handleSeatEntry(PROCultureGUIDEntryDto seat, UsersEntity user) {
        if (seat.getOrderServiceId() != null) {
            assignToOrderService(seat.getOrderServiceId().longValue(), seat.getProCultureGUID(), user);
        } else if (seat.getOrderId() != null && seat.getSeatId() != null) {
            assignToSeatLink(
                    seat.getOrderId().longValue(),
                    seat.getSeatId().longValue(),
                    seat.getProCultureGUID(),
                    user
            );
        } else {
            throw new ProCultureBindingException(
                    "Необходимо указать либо orderServiceId, либо одновременно orderId и seatId"
            );
        }
    }

    private void assignToOrderService(Long orderServiceId, String guid, UsersEntity user) {
        OrderServiceEntity service = orderServiceRepository.findById(orderServiceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderService не найден: " + orderServiceId
                ));
        service.setProCultureGuid(guid);
        orderServiceRepository.save(service);

        logAssignGuid("Привязан ProCulture GUID к OrderService ID: " + orderServiceId, user);
    }

    private void assignToSeatLink(Long orderId, Long seatId, String guid, UsersEntity user) {
        ProCultureLinkEntity link = new ProCultureLinkEntity();
        link.setOrderId(orderId);
        link.setSeatId(seatId);
        link.setProCultureGuid(guid);
        proCultureLinkRepository.save(link);

        logAssignGuid(
                String.format("Привязан ProCulture GUID к Order ID: %d, Seat ID: %d", orderId, seatId),
                user
        );
    }

    private void logAssignGuid(String description, UsersEntity user) {
        actionLogService.save(
                ActionLogEntity.builder()
                        .user(user)
                        .actionType("ASSIGN_GUID")
                        .description(description)
                        .createdAt(LocalDateTime.now())
                        .actorName(user.getUserName())
                        .build()
        );
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
