package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.exception.InvalidOrderRequestException;
import com.ticket.terminal.mapper.SoldServiceMapper;
import com.ticket.terminal.repository.*;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    private final VisitObjectRepository visitObjectRepository;
    private final OrderServiceRepository orderServiceRepository;
    private final OrderServiceVisitorRepository orderServiceVisitorRepository;
    private final SoldRequestEnricherService soldRequestEnricherService;

    @Transactional
    public SoldOrderResponseDto processSoldOrder(SoldOrderRequestDto dto) {
        if (dto.getIsOnlyCheck() == Boolean.TRUE) {
            return performPreCheck(dto);
        } else {
            return confirmOrderPayment(dto);
        }
    }

    private SoldOrderResponseDto performPreCheck(SoldOrderRequestDto dto) {
        return SoldOrderResponseDto.builder()
                .orderId(dto.getOrderId())
                .orderStateId(STATUS_WAITING)
                .orderBarcode(CHECK_MODE_BARCODE)
                .build();
    }

    private SoldOrderResponseDto confirmOrderPayment(SoldOrderRequestDto dto) {
        if (dto.getOrderId() == null || dto.getService().isEmpty()) {
            throw new InvalidOrderRequestException("Список услуг не должен быть пустым");
        }
        soldRequestEnricherService.enrich(dto);

        List<Long> orderServiceIds = dto.getService().stream()
                .map(SoldServiceDto::getOrderServiceId)
                .filter(Objects::nonNull)
                .toList();

        if (orderServiceIds.isEmpty()) {
            throw new InvalidOrderRequestException("Список orderServiceId не должен быть пустым");
        }

        soldServiceRepository.deleteByOrderServiceIds(orderServiceIds);

        dto.setService(
                dto.getService().stream()
                        .peek(serviceDto -> {
                            Long orderServiceId = serviceDto.getOrderServiceId();
                            if (orderServiceId == null) {
                                throw new InvalidOrderRequestException("OrderServiceId не может быть null");
                            }
                            var orderService = orderServiceRepository.findWithServiceById(orderServiceId)
                                    .orElseThrow(() -> new EntityNotFoundException("OrderService не найден: " + orderServiceId));

                            if (serviceDto.getServiceId() == null && orderService.getService() != null) {
                                serviceDto.setServiceId(orderService.getService().getId());
                            }
                        })
                        .toList()
        );

        List<SoldServiceEntity> soldServices = dto.getService().stream()
                .map(serviceDto -> {
                    Long orderServiceId = serviceDto.getOrderServiceId();
                    var orderService = orderServiceRepository.findWithServiceById(orderServiceId)
                            .orElseThrow(() -> new EntityNotFoundException("OrderService не найден: " + orderServiceId));

                    List<Long> visitObjectIds = visitObjectRepository.findByOrderServiceId(orderServiceId).stream()
                            .map(VisitObjectEntity::getId)
                            .toList();
                    serviceDto.setVisitObjectId(visitObjectIds);

                    if (serviceDto.getCategoryVisitor() != null) {
                        for (CategoryVisitorCountDto countDto : serviceDto.getCategoryVisitor()) {
                            orderServiceVisitorRepository.insertVisitor(
                                    orderServiceId,
                                    countDto.getCategoryVisitorId(),
                                    countDto.getVisitorCount()
                            );
                        }
                    }

                    List<VisitObjectEntity> visitObjects = visitObjectIds.stream()
                            .map(id -> visitObjectRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("VisitObject не найден: " + id)))
                            .toList();

                    return SoldServiceEntity.builder()
                            .orderServiceId(orderServiceId)
                            .orderService(orderService)
                            .barcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(orderServiceId))
                            .serviceStateId(SERVICE_STATE_PAID)
                            .paymentKindId(dto.getPaymentKindId())
                            .dtActive(getEndOfDay())
                            .visitObject(visitObjects)
                            .serviceCost(serviceDto.getServiceCost())
                            .serviceCount(serviceDto.getServiceCount())
                            .serviceId(serviceDto.getServiceId())
                            .build();
                })
                .toList();

        soldServiceRepository.saveAll(soldServices);
        soldServiceRepository.flush();

        List<SoldServiceEntity> enrichedServices = soldServiceRepository.findAllWithVisitObjects(orderServiceIds);

        List<SoldServiceDto> serviceDtos = enrichedServices.stream()
                .map(soldServiceMapper::toDto)
                .peek(dtoItem -> {
                    List<CategoryVisitorCountDto> visitorList =
                            orderServiceVisitorRepository.findByOrderServiceId(dtoItem.getOrderServiceId()).stream()
                                    .map(visitor -> new CategoryVisitorCountDto(
                                            visitor.getCategoryVisitor().getId(),
                                            visitor.getVisitorCount(),
                                            visitor.getCategoryVisitorName()))
                                    .toList();
                    dtoItem.setVisitor(visitorList);
                })
                .toList();

        orderRepository.updateOrderState(dto.getOrderId(), STATUS_PAID);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("PAY_ORDER")
                .description(String.format("Оплачен заказ № %s с %d услугами", dto.getOrderId(), soldServices.size()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return SoldOrderResponseDto.builder()
                .orderId(dto.getOrderId())
                .orderStateId(STATUS_PAID)
                .orderBarcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(dto.getOrderId()))
                .orderSiteId(dto.getOrderSiteId())
                .comment(dto.getComment())
                .visitorSiteId(dto.getVisitorSiteId())
                .visitorName1(dto.getVisitorName1())
                .visitorName2(dto.getVisitorName2())
                .visitorName3(dto.getVisitorName3())
                .visitorPhone(dto.getVisitorPhone())
                .visitorMail(dto.getVisitorMail())
                .visitorAddress(dto.getVisitorAddress())
                .visitorDocumentName(dto.getVisitorDocumentName())
                .visitorDocumentSerial(dto.getVisitorDocumentSerial())
                .visitorDocumentNumber(dto.getVisitorDocumentNumber())
                .service(serviceDtos)
                .visitObject(serviceDtos.stream()
                        .flatMap(serviceDto -> serviceDto.getVisitObjectId() == null ? Stream.empty() : serviceDto.getVisitObjectId().stream())
                        .distinct()
                        .toList())
                .build();
    }

    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
