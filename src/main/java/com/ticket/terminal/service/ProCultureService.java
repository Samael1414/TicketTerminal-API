package com.ticket.terminal.service;

import com.ticket.terminal.dto.PROCultureGUIDDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.OrderServiceEntity;
import com.ticket.terminal.entity.ProCultureLinkEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.exception.ProCultureBindingException;
import com.ticket.terminal.repository.OrderServiceRepository;
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

        for (var entry : dto.getSeat()) {
            String guid = entry.getProCultureGUID();

            if (entry.getOrderServiceId() != null) {
                OrderServiceEntity service = orderServiceRepository.findById(entry.getOrderServiceId().longValue())
                        .orElseThrow(() -> new EntityNotFoundException
                                (String.format("OrderService не найден: %s", entry.getOrderServiceId())));
                service.setProCultureGuid(guid);
                orderServiceRepository.save(service);

                actionLogService.save(ActionLogEntity.builder()
                        .user(currentUser)
                        .actionType("ASSIGN_GUID")
                        .description(String.format("Привязан ProCulture GUID к OrderService ID: %s", entry.getOrderServiceId()))
                        .createdAt(LocalDateTime.now())
                        .actorName(currentUser.getUserName())
                        .build());

            } else if (entry.getSeatId() != null && entry.getOrderId() != null) {
                ProCultureLinkEntity entity = new ProCultureLinkEntity();
                entity.setOrderId(entry.getOrderId().longValue());
                entity.setSeatId(entry.getSeatId().longValue());
                entity.setProCultureGuid(guid);
                proCultureLinkRepository.save(entity);

                actionLogService.save(ActionLogEntity.builder()
                        .user(currentUser)
                        .actionType("ASSIGN_GUID")
                        .description(String.format("Привязан ProCulture GUID к Order ID: %s, Seat ID: %s",
                                entry.getOrderId(), entry.getSeatId()))
                        .createdAt(LocalDateTime.now())
                        .actorName(currentUser.getUserName())
                        .build());
            } else {
                throw new ProCultureBindingException("Необходимо указать либо orderServiceId, либо (orderId + seatId)");
            }
        }
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
