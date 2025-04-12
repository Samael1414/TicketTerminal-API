package com.ticket.terminal.service;

import com.ticket.terminal.dto.SoldOrderRequestDto;
import com.ticket.terminal.dto.SoldOrderResponseDto;
import com.ticket.terminal.dto.SoldServiceDto;
import com.ticket.terminal.entity.SoldServiceEntity;
import com.ticket.terminal.mapper.SoldServiceMapper;
import com.ticket.terminal.repository.OrderRepository;
import com.ticket.terminal.repository.SoldServiceRepository;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
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
            throw new IllegalArgumentException("The list of services must not be empty");
        }

        List<Long> orderServiceIds = dto.getService().stream().map(SoldServiceDto::getOrderServiceId).toList();
        soldServiceRepository.deleteByOrderServiceIds(orderServiceIds);

        List<SoldServiceEntity> soldServices = dto.getService().stream().map(serviceDto -> {
            SoldServiceEntity entity = new SoldServiceEntity();
            entity.setOrderServiceId(serviceDto.getOrderServiceId());
            entity.setBarcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(serviceDto.getOrderServiceId()));
            entity.setServiceStateId(SERVICE_STATE_PAID); //оплачено
            entity.setPaymentKindId(dto.getPaymentKindId());
            // Устанавливаем срок действия билета до конца текущего дня (23:59:59)
            entity.setDtActive(getEndOfDay());
            return entity;
        }).toList();
        soldServiceRepository.saveAll(soldServices);

        orderRepository.updateOrderState(dto.getOrderId(), STATUS_PAID);

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
}
