package com.ticket.terminal.service;

import com.ticket.terminal.dto.SoldOrderRequestDto;
import com.ticket.terminal.dto.SoldOrderResponseDto;
import com.ticket.terminal.dto.SoldServiceDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.SoldServiceEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.exception.InvalidOrderRequestException;
import com.ticket.terminal.mapper.SoldServiceMapper;
import com.ticket.terminal.repository.OrderRepository;
import com.ticket.terminal.repository.SoldServiceRepository;
import com.ticket.terminal.repository.UserRepository;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoldOrderService {

    private static final String CHECK_MODE_BARCODE = "CHECK-MODE";
    private static final int STATUS_WAITING = 1;
    private static final int STATUS_PAID = 2;
    private static final int SERVICE_STATE_PAID = 5;


    private final SoldServiceRepository soldServiceRepository;
    private final SoldServiceMapper soldServiceMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;


    @Transactional
    public SoldOrderResponseDto processSoldOrder(SoldOrderRequestDto dto) {
        if (dto.getIsOnlyCheck() == 1) {
            return performPreCheck(dto);
        } else {
            return confirmOrderPayment(dto);
        }
    }

    private SoldOrderResponseDto performPreCheck(SoldOrderRequestDto dto) {
        return SoldOrderResponseDto.builder().orderId(dto.getOrderId()).orderStateId(STATUS_WAITING) // 1 = ожидание оплаты
                .orderBarcode(CHECK_MODE_BARCODE).build();
    }


    private SoldOrderResponseDto confirmOrderPayment(SoldOrderRequestDto dto) {
        if (dto.getOrderId() == null || dto.getService().isEmpty()) {
            throw new InvalidOrderRequestException("Список услуг не должен быть пустым");
        }

        List<Long> orderServiceIds = dto.getService().stream().map(SoldServiceDto::getOrderServiceId).toList();
        soldServiceRepository.deleteByOrderServiceIds(orderServiceIds);

        List<SoldServiceEntity> soldServices = dto.getService().stream().map(serviceDto -> {
            SoldServiceEntity entity = SoldServiceEntity
                    .builder()
                    .orderServiceId(serviceDto.getOrderServiceId())
                    .barcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(serviceDto.getOrderServiceId()))
                    .serviceStateId(SERVICE_STATE_PAID) //оплачено
                    .paymentKindId(dto.getPaymentKindId())
                    .dtActive(getEndOfDay()) // Устанавливаем срок действия билета до конца текущего дня (23:59:59)
                    .build();
            return entity;
        }).toList();
        soldServiceRepository.saveAll(soldServices);

        orderRepository.updateOrderState(dto.getOrderId(), STATUS_PAID);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("PAY_ORDER")
                .description(String.format("Оплачен заказ № %s с %d услугами",
                        dto.getOrderId(), soldServices.size()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return SoldOrderResponseDto.builder().orderId(dto.getOrderId()).orderStateId(STATUS_PAID) // оплачено
                .orderBarcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(dto.getOrderId()))
                .service(soldServices.stream()
                        .map(soldServiceMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
