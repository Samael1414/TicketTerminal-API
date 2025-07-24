package com.ticket.terminal.service;

import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import com.ticket.terminal.dto.sold.SoldOrderRequestDto;
import com.ticket.terminal.dto.sold.SoldOrderResponseDto;
import com.ticket.terminal.dto.sold.SoldServiceDto;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.entity.order.OrderServiceEntity;
import com.ticket.terminal.exception.InvalidOrderRequestException;
import com.ticket.terminal.mapper.SoldServiceMapper;
import com.ticket.terminal.repository.*;
import com.ticket.terminal.repository.order.OrderRepository;
import com.ticket.terminal.repository.order.OrderServiceRepository;
import com.ticket.terminal.repository.order.OrderServiceVisitorRepository;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoldOrderService {

    private static final String CHECK_MODE_BARCODE = "CHECK-MODE";
    private static final int STATUS_WAITING = 1;
    private static final int STATUS_PAID = 5;
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
        return Boolean.TRUE.equals(dto.getIsOnlyCheck())
                ? preCheckResponse(dto)
                : confirmPayment(dto);
    }

    private SoldOrderResponseDto preCheckResponse(SoldOrderRequestDto dto) {
        return SoldOrderResponseDto.builder()
                .orderId(dto.getOrderId())
                .orderStateId(STATUS_WAITING)
                .orderBarcode(CHECK_MODE_BARCODE)
                .build();
    }

    private SoldOrderResponseDto confirmPayment(SoldOrderRequestDto dto) {
        validateRequest(dto);

        // обогащаем dto недостающими данными
        soldRequestEnricherService.enrich(dto);

        List<Long> orderServiceIds = extractServiceIds(dto);
        deleteExistingSoldServices(orderServiceIds);

        // единоразово загрузим все OrderServiceEntity
        var serviceEntityMap = loadOrderServices(orderServiceIds);

        // 1) создаём новые SoldServiceEntity
        List<SoldServiceEntity> entities = buildSoldEntities(dto, serviceEntityMap);

        // 2) сохраняем и сбрасываем изменения
        soldServiceRepository.saveAll(entities);
        soldServiceRepository.flush();

        // 3) получаем сохранённые записи с visitObjects
        List<SoldServiceEntity> saved = soldServiceRepository
                .findAllWithVisitObjects(orderServiceIds);

        // 4) мапим в DTO
        List<SoldServiceDto> services = mapToDtosWithVisitors(saved);

        // 5) переводим заказ в статус «оплачен»
        orderRepository.updateOrderState(dto.getOrderId(), STATUS_PAID);

        // 6) логируем
        logPayment(dto.getOrderId(), services.size());

        // 7) собираем и возвращаем ответ
        return buildResponse(dto, services);
    }

    private void validateRequest(SoldOrderRequestDto dto) {
        if (dto.getOrderId() == null || dto.getService().isEmpty()) {
            throw new InvalidOrderRequestException("Список услуг не может быть пустым");
        }
    }

    private List<Long> extractServiceIds(SoldOrderRequestDto dto) {
        List<Long> ids = dto.getService().stream()
                .map(SoldServiceDto::getOrderServiceId)
                .filter(Objects::nonNull)
                .toList();
        if (ids.isEmpty()) {
            throw new InvalidOrderRequestException("OrderServiceId не может быть пустым");
        }
        return ids;
    }

    private void deleteExistingSoldServices(List<Long> ids) {
        soldServiceRepository.deleteByOrderServiceIds(ids);
    }

    private Map<Long, OrderServiceEntity> loadOrderServices(List<Long> ids) {
        return orderServiceRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(OrderServiceEntity::getId, orderServiceEntity -> orderServiceEntity));
    }

    private List<SoldServiceEntity> buildSoldEntities(
            SoldOrderRequestDto dto,
            Map<Long, OrderServiceEntity> entityMap
    ) {
        return dto.getService().stream().map(soldServiceDto -> {
            Long orderServiceIds = Optional.ofNullable(soldServiceDto.getOrderServiceId())
                    .orElseThrow(() -> new InvalidOrderRequestException("OrderServiceId is null"));
            OrderServiceEntity orderServiceEntity = Optional.ofNullable(entityMap.get(orderServiceIds))
                    .orElseThrow(() -> new EntityNotFoundException("OrderService not found: " + orderServiceIds));

            // Вставляем categoryVisitors сразу в БД
            Optional.ofNullable(soldServiceDto.getCategoryVisitor()).ifPresent(list ->
                    list.forEach(visitorCountDto -> orderServiceVisitorRepository.insertVisitor(
                            orderServiceIds, visitorCountDto.getCategoryVisitorId(), visitorCountDto.getVisitorCount()))
            );

            // Формируем сущность
            return SoldServiceEntity.builder()
                    .orderServiceId(orderServiceIds)
                    .orderService(orderServiceEntity)
                    .barcode(BarcodeGeneratorUtil.generateSoldServiceBarcode(orderServiceIds))
                    .serviceStateId(SERVICE_STATE_PAID)
                    .paymentKindId(dto.getPaymentKindId())
                    .dtActive(getEndOfDay())
                    .visitObject(fetchVisitObjects(orderServiceIds))
                    .serviceCost(soldServiceDto.getServiceCost())
                    .serviceCount(soldServiceDto.getServiceCount())
                    .serviceId(soldServiceDto.getServiceId())
                    .build();
        }).toList();
    }

    private List<VisitObjectEntity> fetchVisitObjects(Long osId) {
        return visitObjectRepository.findByOrderServiceId(osId);
    }

    private List<SoldServiceDto> mapToDtosWithVisitors(List<SoldServiceEntity> entities) {
        return entities.stream()
                .map(soldServiceMapper::toDto)
                .peek(dto -> {
                    List<CategoryVisitorCountDto> visitors =
                            orderServiceVisitorRepository
                                    .findByOrderServiceId(dto.getOrderServiceId())
                                    .stream()
                                    .map(visitorEntity -> new CategoryVisitorCountDto(
                                            visitorEntity.getCategoryVisitor().getId(),
                                            visitorEntity.getVisitorCount(),
                                            visitorEntity.getCategoryVisitorName()))
                                    .toList();
                    dto.setVisitor(visitors);
                })
                .toList();
    }

    private void logPayment(Long orderId, int serviceCount) {
        UsersEntity user = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(user)
                .actionType("PAY_ORDER")
                .description("Оплачен заказ № " + orderId + " услуг: " + serviceCount)
                .createdAt(LocalDateTime.now())
                .actorName(user.getUserName())
                .build());
    }

    private SoldOrderResponseDto buildResponse(
            SoldOrderRequestDto dto,
            List<SoldServiceDto> services
    ) {
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
                .service(services)
                .visitObject(services.stream()
                        .flatMap(s -> s.getVisitObjectId().stream())
                        .distinct()
                        .toList())
                .build();
    }

    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59);
    }

    private UsersEntity getCurrentUser() {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
