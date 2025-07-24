package com.ticket.terminal.service;

import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import com.ticket.terminal.dto.editable.EditableOrderRequestDto;
import com.ticket.terminal.dto.editable.EditableOrderServiceDto;
import com.ticket.terminal.dto.order.*;
import com.ticket.terminal.dto.simple.SimpleOrderRequestDto;
import com.ticket.terminal.dto.simple.SimpleOrderServiceDto;
import com.ticket.terminal.dto.sold.SoldOrderRequestDto;
import com.ticket.terminal.dto.sold.SoldServiceDto;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.entity.order.*;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.exception.EmptyRefundListException;
import com.ticket.terminal.exception.InvalidRefundRequestException;
import com.ticket.terminal.exception.PartialCancellationException;
import com.ticket.terminal.mapper.OrderMapper;
import com.ticket.terminal.repository.*;
import com.ticket.terminal.repository.order.OrderRepository;
import com.ticket.terminal.repository.order.OrderServiceRepository;
import com.ticket.terminal.repository.order.OrderServiceVisitObjectRepository;
import com.ticket.terminal.repository.order.OrderServiceVisitorRepository;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;
    private final OrderServiceVisitorRepository orderServiceVisitorRepository;
    private final OrderServiceVisitObjectRepository orderServiceVisitObjectRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final OrderMapper orderMapper;
    private final OrderServiceRepository orderServiceRepository;
    private final SoldServiceRepository soldServiceRepository;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    public OrderDto getOrderById(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден: " + orderId));

        return orderMapper.toDto(entity);
    }

    public OrderResponseDto getOrderByDateRange(String dtBegin, String dtEnd) {
        LocalDateTime start = parseStart(dtBegin);
        LocalDateTime end   = parseEnd(dtEnd);

        List<OrderDto> orderDtos = orderRepository
                .findOrdersCreatedBetween(start, end)
                .stream()
                .map(orderMapper::toDto)
                .toList();

        return OrderResponseDto.builder()
                .order(orderDtos)
                .build();
    }


    @Transactional
    public OrderCreateResponseDto createSimpleOrder(SimpleOrderRequestDto requestDto) {
        // 1) Подготовка сущности заказа (инициализация, id, штрихкод, services)
        OrderEntity order = prepareOrder(requestDto);

        // 2) Сохраняем сам заказ вместе со всеми OrderServiceEntity
        OrderEntity savedOrder = orderRepository.save(order);

        // 3) Пишем в БД связи OrderService ↔ VisitObject и ↔ CategoryVisitor
        persistServiceDetails(savedOrder, requestDto);

        // 4) Собираем SoldOrderRequestDto (то, что уйдёт в response)
        SoldOrderRequestDto soldRequest = buildSoldRequest(savedOrder);

        // 5) Логируем факт создания заказа
        logAction(
                savedOrder,
                "CREATE_ORDER",
                "Создан заказ № " + savedOrder.getId()
        );

        // 6) Мапим и возвращаем итоговый DTO
        OrderCreateResponseDto response = orderMapper.toResponseDto(savedOrder);
        response.setSoldRequest(soldRequest);
        return response;
    }



    @Transactional
    public OrderCreateResponseDto createEditableOrder(EditableOrderRequestDto dto) {
        // 1) Подготовить сущность заказа
        OrderEntity order = prepareEditableOrder(dto);

        // 2) Сохранить заказ со всеми OrderServiceEntity
        OrderEntity savedOrder = orderRepository.save(order);

        // 3) Сохранить связи VisitObject и CategoryVisitor
        persistServiceDetails(savedOrder, dto);

        // 4) Построить SoldOrderRequestDto
        SoldOrderRequestDto soldRequest = buildSoldRequest(savedOrder);

        // 5) Залогировать создание
        logAction(
                savedOrder,
                "CREATE_EDITABLE_ORDER",
                "Создан изменяемый заказ № " + savedOrder.getId()
        );

        // 6) Вернуть ответ
        OrderCreateResponseDto response = orderMapper.toResponseDto(savedOrder);
        response.setSoldRequest(soldRequest);
        return response;
    }


    @Transactional
    public OrderDto cancelOrder(OrderCancelDto dto) {
        Long orderId = dto.getOrderId();
        OrderEntity order = findOrderById(orderId);

        if (isFullCancellation(dto)) {
            cancelAllServices(order);
        } else {
            cancelSelectedServices(order, dto.getOrderServiceId());
        }

        orderRepository.save(order);
        logAction(order, "CANCEL_ORDER", "Отменён заказ № " + orderId);
        return safeMapToDto(order);
    }


    @Transactional
    public OrderDto refundOrder(OrderRefundDto dto) {
        // 1) Загружаем заказ или кидаем 404
        OrderEntity order = findOrderById(dto.getOrderId());

        // 2) Определяем, какие услуги возвращать
        List<Long> serviceIds = determineRefundServiceIds(dto, order);

        // 3) Выполняем возврат: отмечаем в БД и меняем состояние в сущности
        performRefund(order, serviceIds);

        // 4) Сохраняем изменения самого заказа
        orderRepository.save(order);

        // 5) Логируем операцию
        logAction(
                order,
                "REFUND_ORDER",
                String.format("Возврат услуг по заказу № %s", order.getOrderId())
        );

        // 6) Безопасно мапим в DTO с повторным подгрузом при Lazy
        return safeMapToDto(order);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }

    private Long generateUniqueOrderId() {
        Long maxOrderId = orderRepository.findMaxOrderId();
        return maxOrderId + 1;
    }

    private LocalDateTime parseStart(String dtStart) {
        return LocalDate.parse(dtStart, DATE_FORMATTER)
                .atStartOfDay();
    }

    private LocalDateTime parseEnd(String dtEnd) {
        return LocalDate.parse(dtEnd, DATE_FORMATTER)
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);
    }

    private List<OrderServiceEntity> buildOrderServices(SimpleOrderRequestDto dto, OrderEntity order) {
        return dto.getService().stream()
                .map(simpleOrderServiceDto -> {
                    ServiceEntity service = fetchServiceEntity(simpleOrderServiceDto.getServiceId());
                    validateServiceRules(simpleOrderServiceDto, service);
                    return mapToOrderServiceEntity(simpleOrderServiceDto, service, order);
                })
                .collect(Collectors.toList());
    }

    // 1) Достаём ServiceEntity или бросаем ошибку
    private ServiceEntity fetchServiceEntity(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Service не найден: %s", serviceId)));
    }

    // 2) Проверяем бизнес-правила в одном месте
    private void validateServiceRules(SimpleOrderServiceDto orderServiceDto, ServiceEntity service) {
        if (Boolean.TRUE.equals(service.getIsNeedVisitDate()) && orderServiceDto.getDtVisit() == null) {
            throw new IllegalArgumentException(
                    String.format("dtVisit обязателен для услуги ID %s", service.getId()));
        }
        if (service.getIsVisitObjectUseForCost()
                && (orderServiceDto.getVisitObjectId() == null || orderServiceDto.getVisitObjectId().isEmpty())) {
            throw new IllegalArgumentException(
                    String.format("VisitObjectId обязателен для услуги ID %s", service.getId()));
        }
        if (service.getIsVisitorCountUseForCost()
                && (orderServiceDto.getCategoryVisitor() == null || orderServiceDto.getCategoryVisitor().isEmpty())) {
            throw new IllegalArgumentException(
                    String.format("CategoryVisitor обязателен для услуги ID %s", service.getId()));
        }
    }

    // 3) Собираем и заполняем новый OrderServiceEntity
    private OrderServiceEntity mapToOrderServiceEntity(
            SimpleOrderServiceDto orderServiceDto,
            ServiceEntity service,
            OrderEntity order
    ) {
        return OrderServiceEntity.builder()
                .service(service)
                .order(order)
                .dtVisit(toLocalDateTime(orderServiceDto.getDtVisit()))
                .serviceCount(orderServiceDto.getServiceCount())
                .cost(Optional.ofNullable(orderServiceDto.getServiceCost()).orElse(0.0))
                .serviceStateId(ServiceState.ORDERED.getCode())
                .serviceName(service.getServiceName())
                .build();
    }

    private LocalDateTime toLocalDateTime(ZonedDateTime time) {
        return time != null ? time.toLocalDateTime() : null;
    }

    private OrderEntity prepareOrder(SimpleOrderRequestDto dto) {
        OrderEntity order = orderMapper.toEntity(dto);
        OrderEntityUtil.initialize(order);

        if (order.getOrderId() == null) {
            order.setOrderId(generateUniqueOrderId());
        }
        if (!StringUtils.hasText(order.getOrderBarcode())) {
            order.setOrderBarcode(
                    BarcodeGeneratorUtil.generateUniqueOrderBarcode(orderRepository)
            );
        }

        List<OrderServiceEntity> services = buildOrderServices(dto, order);
        order.setService(services);
        return order;
    }

    private void persistServiceDetails(OrderEntity order, SimpleOrderRequestDto dto) {
        List<OrderServiceEntity> services = order.getService();
        List<SimpleOrderServiceDto> dtos = dto.getService();

        for (int i = 0; i < services.size(); i++) {
            OrderServiceEntity orderServiceEntity = services.get(i);
            SimpleOrderServiceDto simpleOrderServiceDto = dtos.get(i);
            persistVisitObjects(orderServiceEntity, simpleOrderServiceDto);
            persistCategoryVisitors(orderServiceEntity, simpleOrderServiceDto);
        }
    }

    private void persistVisitObjects(OrderServiceEntity orderServiceEntity, SimpleOrderServiceDto simpleOrderServiceDto) {
        Optional.ofNullable(simpleOrderServiceDto.getVisitObjectId())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(id -> visitObjectRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "VisitObject не найден: " + id)))
                .map(visitObject -> OrderServiceVisitObjectEntity.builder()
                        .orderService(orderServiceEntity).visitObject(visitObject).build())
                .forEach(orderServiceVisitObjectRepository::save);
    }

    private void persistCategoryVisitors(
            OrderServiceEntity orderServiceEntity,
            SimpleOrderServiceDto simpleOrderServiceDto
    ) {
        // Получаем список или пустой
        List<CategoryVisitorCountDto> dtos = Optional.ofNullable(simpleOrderServiceDto.getCategoryVisitor())
                .orElseGet(Collections::emptyList);

        // Пробегаемся по каждому DTO и сохраняем связь
        for (CategoryVisitorCountDto visitorCountDto : dtos) {
            // 1) Загружаем CategoryVisitorEntity или кидаем 404
            CategoryVisitorEntity categoryVisitor = categoryVisitorRepository.findById(visitorCountDto.getCategoryVisitorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "CategoryVisitor не найден: " + visitorCountDto.getCategoryVisitorId()
                    ));

            // 2) Строим связь
            OrderServiceVisitorEntity visitorEntity = OrderServiceVisitorEntity.builder()
                    .orderService(orderServiceEntity)
                    .categoryVisitor(categoryVisitor)
                    .visitorCount(visitorCountDto.getVisitorCount())
                    .categoryVisitorName(categoryVisitor.getCategoryVisitorName())
                    .build();

            // 3) Сохраняем
            orderServiceVisitorRepository.save(visitorEntity);
        }
    }


    private SoldOrderRequestDto buildSoldRequest(OrderEntity order) {
        List<SoldServiceDto> soldServices = order.getService().stream()
                .map(this::toSoldServiceDto)
                .toList();

        return SoldOrderRequestDto.builder()
                .orderId(order.getId())
                .service(soldServices)
                .build();
    }

    private SoldServiceDto toSoldServiceDto(OrderServiceEntity orderServiceEntity) {
        // 1) Собираем id visitObject
        List<Long> visitIds = visitObjectRepository
                .findByOrderServiceId(orderServiceEntity.getId())
                .stream()
                .map(VisitObjectEntity::getId)
                .toList();

        // 2) Собираем CategoryVisitorCountDto
        List<CategoryVisitorCountDto> visitors = orderServiceVisitorRepository
                .findByOrderServiceId(orderServiceEntity.getId())
                .stream()
                .map(visitorEntity -> new CategoryVisitorCountDto(
                        visitorEntity.getCategoryVisitor().getId(),
                        visitorEntity.getVisitorCount(),
                        visitorEntity.getCategoryVisitorName()))
                .toList();

        // 3) Строим DTO через билдeр
        return SoldServiceDto.builder()
                .orderServiceId(orderServiceEntity.getId())
                .serviceId(orderServiceEntity.getService().getId())
                .serviceCost(orderServiceEntity.getCost())
                .serviceCount(orderServiceEntity.getServiceCount())
                .dtVisit(orderServiceEntity.getDtVisit())
                .visitObjectId(visitIds)
                .visitObject(visitIds)
                .categoryVisitor(visitors)
                .build();
    }


    private void logCreation(OrderEntity savedOrder) {
        UsersEntity user = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(user)
                .actionType("CREATE_ORDER")
                .description("Создан заказ № " + savedOrder.getId())
                .createdAt(LocalDateTime.now())
                .actorName(user.getUserName())
                .build());
    }

    private List<OrderServiceEntity> buildEditableOrderServices(
            EditableOrderRequestDto dto,
            OrderEntity order
    ) {
        return dto.getService().stream()
                .map(editableDto -> {
                    ServiceEntity service = fetchServiceEntity(editableDto.getServiceId());
                    validateEditableRules(editableDto, service);
                    return mapToOrderServiceEntity(editableDto, service, order);
                })
                .toList();
    }


    // 2) Проверяем обязательные поля для EditableOrderServiceDto
    private void validateEditableRules(
            EditableOrderServiceDto dto,
            ServiceEntity service
    ) {
        if (Boolean.TRUE.equals(service.getIsNeedVisitDate())
                && dto.getDtVisit() == null) {
            throw new IllegalArgumentException(
                    "dtVisit обязателен для услуги ID " + service.getId());
        }
        if (service.getIsVisitObjectUseForCost()
                && (dto.getVisitObjectId() == null
                || dto.getVisitObjectId().isEmpty())) {
            throw new IllegalArgumentException(
                    "VisitObjectId обязателен для услуги ID " + service.getId());
        }
        if (service.getIsVisitorCountUseForCost()
                && (dto.getCategoryVisitor() == null
                || dto.getCategoryVisitor().isEmpty())) {
            throw new IllegalArgumentException(
                    "CategoryVisitor обязателен для услуги ID " + service.getId());
        }
    }

    // 3) Маппинг EditableOrderServiceDto → OrderServiceEntity через билдeр
    private OrderServiceEntity mapToOrderServiceEntity(
            EditableOrderServiceDto dto,
            ServiceEntity service,
            OrderEntity order
    ) {
        return OrderServiceEntity.builder()
                .service(service)
                .order(order)
                .dtVisit(toLocalDateTime(dto.getDtVisit()))
                .serviceCount(dto.getServiceCount())
                .cost(Optional.ofNullable(dto.getServiceCost()).orElse(0.0))
                .serviceStateId(ServiceState.ORDERED.getCode())
                .serviceName(service.getServiceName())
                .build();
    }

    // 1) Подготовка сущности заказа
    private OrderEntity prepareEditableOrder(EditableOrderRequestDto dto) {
        OrderEntity order = orderMapper.toEntity(dto);
        OrderEntityUtil.initialize(order);

        if (order.getOrderId() == null) {
            order.setOrderId(generateUniqueOrderId());
        }
        if (!StringUtils.hasText(order.getOrderBarcode())) {
            order.setOrderBarcode(
                    BarcodeGeneratorUtil.generateUniqueOrderBarcode(orderRepository)
            );
        }
        if (orderRepository.existsByOrderBarcode(order.getOrderBarcode())) {
            throw new IllegalStateException(
                    "Штрихкод уже существует: " + order.getOrderBarcode()
            );
        }

        List<OrderServiceEntity> services = buildEditableOrderServices(dto, order);
        order.setService(services);
        return order;
    }

    private void persistServiceDetails(
            OrderEntity order,
            EditableOrderRequestDto dto
    ) {
        List<OrderServiceEntity> services = order.getService();
        List<EditableOrderServiceDto> dtos = dto.getService();

        for (int i = 0; i < services.size(); i++) {
            OrderServiceEntity orderServiceEntity = services.get(i);
            EditableOrderServiceDto editableOrder = dtos.get(i);

            persistVisitObjects(orderServiceEntity, editableOrder.getVisitObjectId());
            persistCategoryVisitors(orderServiceEntity, editableOrder.getCategoryVisitor());
        }
    }

    private void persistVisitObjects(
            OrderServiceEntity orderServiceEntity,
            List<Long> visitIds
    ) {
        Optional.ofNullable(visitIds).orElseGet(List::of)
                .stream()
                .map(id -> visitObjectRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "VisitObject не найден: " + id)))
                .map(visitObject -> OrderServiceVisitObjectEntity.builder()
                        .orderService(orderServiceEntity)
                        .visitObject(visitObject)
                        .build())
                .forEach(orderServiceVisitObjectRepository::save);
    }

    private void persistCategoryVisitors(
            OrderServiceEntity orderServiceEntity,
            List<CategoryVisitorCountDto> visitors
    ) {
        Optional.ofNullable(visitors).orElseGet(List::of)
                .stream()
                .map(visitorCountDto -> {
                    CategoryVisitorEntity visitorEntity = categoryVisitorRepository.findById(visitorCountDto.getCategoryVisitorId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "CategoryVisitor не найден: " + visitorCountDto.getCategoryVisitorId()));
                    return OrderServiceVisitorEntity.builder()
                            .orderService(orderServiceEntity)
                            .categoryVisitor(visitorEntity)
                            .visitorCount(visitorCountDto.getVisitorCount())
                            .categoryVisitorName(visitorEntity.getCategoryVisitorName())
                            .build();
                })
                .forEach(orderServiceVisitorRepository::save);
    }

    private OrderEntity findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Заказ не найден: " + orderId));
    }

    private void cancelAllServices(OrderEntity order) {
        order.getService().forEach(serviceEntity ->
                serviceEntity.setServiceStateId(ServiceState.RETURNED.getCode())
        );
    }

    // Частичная отмена: удаляем связи, сами сервисы и из заказа
    private void cancelSelectedServices(OrderEntity order, List<Long> ids) {
        List<OrderServiceEntity> toRemove = order.getService().stream()
                .filter(serviceEntity -> ids.contains(serviceEntity.getId()))
                .toList();

        if (toRemove.isEmpty()) {
            throw new PartialCancellationException(
                    "Не найдено услуг для частичной отмены"
            );
        }

        List<Long> serviceEntityIds = toRemove.stream()
                .map(OrderServiceEntity::getId)
                .toList();

        orderServiceVisitorRepository.deleteByOrderServiceIdIn(serviceEntityIds);
        orderServiceVisitObjectRepository.deleteByOrderServiceIdIn(serviceEntityIds);
        orderServiceRepository.deleteAll(toRemove);
        order.getService().removeAll(toRemove);
    }

    // Безопасный toDto с повторной попыткой
    private OrderDto safeMapToDto(OrderEntity order) {
        try {
            return orderMapper.toDto(order);
        } catch (Exception ex) {
            // повторно подгружаем, если было Lazy-exception
            OrderEntity fresh = findOrderById(order.getOrderId());
            return orderMapper.toDto(fresh);
        }
    }

    private boolean isFullCancellation(OrderCancelDto dto) {
        return dto.getOrderServiceId() == null
                || dto.getOrderServiceId().isEmpty();
    }

    private void logAction(OrderEntity order, String actionType, String description) {
        UsersEntity user = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(user)
                .actionType(actionType)
                .description(description)
                .createdAt(LocalDateTime.now())
                .actorName(user.getUserName())
                .build());
    }

    private List<Long> determineRefundServiceIds(OrderRefundDto dto, OrderEntity order) {
        List<Long> allIds = orderServiceRepository.findAllByOrderId(order.getOrderId())
                .stream().map(OrderServiceEntity::getId).toList();

        if (dto.getServices() == null || dto.getServices().isEmpty()) {
            return allIds;
        }

        List<Long> requested = dto.getServices().stream()
                .map(OrderRefundServiceDto::getOrderServiceId)
                .toList();

        if (!allIds.containsAll(requested)) {
            throw new InvalidRefundRequestException(
                    "Некоторые услуги не относятся к заказу: " + order.getOrderId()
            );
        }
        return requested;
    }

    private void performRefund(OrderEntity order, List<Long> serviceIds) {
        if (serviceIds.isEmpty()) {
            throw new EmptyRefundListException(
                    "Не найдено ни одной услуги для возврата"
            );
        }

        // отмечаем в таблице sold_service
        soldServiceRepository.markAsRefunded(serviceIds);

        // меняем состояние в сущности, чтобы при дальнейшей маппинге оно попало в DTO
        order.getService().stream()
                .filter(serviceEntity -> serviceIds.contains(serviceEntity.getId()))
                .forEach(serviceEntity -> serviceEntity.setServiceStateId(ServiceState.RETURNED.getCode()));
    }




}
