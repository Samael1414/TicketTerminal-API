package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.exception.EmptyRefundListException;
import com.ticket.terminal.exception.InvalidRefundRequestException;
import com.ticket.terminal.exception.PartialCancellationException;
import com.ticket.terminal.mapper.OrderMapper;
import com.ticket.terminal.mapper.OrderServiceMapper;
import com.ticket.terminal.repository.*;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;
    private final OrderServiceVisitorRepository orderServiceVisitorRepository;
    private final OrderServiceVisitObjectRepository orderServiceVisitObjectRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final OrderMapper orderMapper;
    private final OrderServiceMapper orderServiceMapper;
    private final OrderServiceRepository orderServiceRepository;
    private final SoldServiceRepository soldServiceRepository;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    public OrderDto getOrderById(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
        return orderMapper.toDto(entity);
    }

    public OrderResponseDto getOrderByDateRange(String dtBegin, String dtEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime begin = LocalDate.parse(dtBegin, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(dtEnd, formatter).plusDays(1).atStartOfDay().minusNanos(1);

        List<OrderDto> orderDtos;
        try (Stream<OrderEntity> stream = orderRepository.findOrdersByDtVisitBetween(begin, end).stream()) {
            orderDtos = stream.map(orderMapper::toDto).toList();
        }
        return OrderResponseDto.builder().order(orderDtos).build();
    }


    @Transactional
    public OrderCreateResponseDto createSimpleOrder(SimpleOrderRequestDto requestDto) {
        log.debug(">>> createSimpleOrder()");
        logVal("requestDto", requestDto);
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        OrderEntityUtil.initialize(orderEntity);

        if (orderEntity.getOrderId() == null) {
            orderEntity.setOrderId(generateUniqueOrderId());
            logVal("generated orderId", orderEntity.getOrderId());
        }

        if (orderEntity.getOrderBarcode() == null || orderEntity.getOrderBarcode().isEmpty()) {
            orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateUniqueOrderBarcode(orderRepository));
            logVal("generated barcode", orderEntity.getOrderBarcode());
        }

        List<OrderServiceEntity> orderServiceEntities = buildOrderServices(requestDto, orderEntity);
        orderEntity.setService(orderServiceEntities);
        logVal("orderServiceEntities.size", orderServiceEntities.size());

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        logVal("savedOrder.id", savedOrder.getId());

        for (int i = 0; i < orderServiceEntities.size(); i++) {
            OrderServiceEntity orderServiceEntity = orderServiceEntities.get(i);
            SimpleOrderServiceDto serviceDto = requestDto.getService().get(i);

            if (serviceDto.getVisitObjectId() != null) {
                for (Long visitObjects : serviceDto.getVisitObjectId()) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(visitObjects)
                            .orElseThrow(() -> new EntityNotFoundException("VisitObject не найден: " + visitObjects));

                    OrderServiceVisitObjectEntity osv = OrderServiceVisitObjectEntity.builder()
                            .orderService(orderServiceEntity)
                            .visitObject(visitObject)
                            .build();

                    orderServiceVisitObjectRepository.save(osv);
                }
            }

            if (serviceDto.getCategoryVisitor() != null) {
                for (CategoryVisitorCountDto catDto : serviceDto.getCategoryVisitor()) {
                    CategoryVisitorEntity catEntity = categoryVisitorRepository.findById(catDto.getCategoryVisitorId())
                            .orElseThrow(() -> new EntityNotFoundException("CategoryVisitor не найден: " + catDto.getCategoryVisitorId()));

                    OrderServiceVisitorEntity osv = OrderServiceVisitorEntity.builder()
                            .orderService(orderServiceEntity)
                            .categoryVisitor(catEntity)
                            .visitorCount(catDto.getVisitorCount())
                            .build();

                    orderServiceVisitorRepository.save(osv);
                }
            }
        }

        List<SoldServiceDto> soldServices = orderServiceEntities.stream().map(orderService -> {
            SoldServiceDto dto = new SoldServiceDto();
            dto.setOrderServiceId(orderService.getId());
            dto.setServiceId(orderService.getService().getId());
            dto.setServiceCost(orderService.getCost());
            dto.setServiceCount(orderService.getServiceCount());
            dto.setDtVisit(orderService.getDtVisit());

            List<Long> visitObjectIds = visitObjectRepository.findByOrderServiceId(orderService.getId())
                    .stream().map(VisitObjectEntity::getId).toList();
            dto.setVisitObjectId(visitObjectIds);
            dto.setVisitObject(visitObjectIds);

            List<CategoryVisitorCountDto> visitors = orderServiceVisitorRepository.findByOrderServiceId(orderService.getId())
                    .stream()
                    .map(v -> new CategoryVisitorCountDto(v.getCategoryVisitor().getId(), v.getVisitorCount()))
                    .toList();
            dto.setCategoryVisitor(visitors);

            return dto;
        }).toList();
        logVal("soldServices.size", soldServices.size());

        SoldOrderRequestDto soldRequest = new SoldOrderRequestDto();
        soldRequest.setOrderId(savedOrder.getId());
        soldRequest.setService(soldServices);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_ORDER")
                .description("Создан заказ № " + savedOrder.getId())
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        OrderCreateResponseDto response = orderMapper.toResponseDto(savedOrder);
        response.setSoldRequest(soldRequest);
        logVal("OrderCreateResponseDto (out)", response);
        return response;
    }


    private List<OrderServiceEntity> buildOrderServices(SimpleOrderRequestDto dto, OrderEntity order) {
        log.debug("--- buildOrderServices() ---");
        logVal("dto.service.size", dto.getService().size());
        return dto.getService().stream()
                .map(serviceDto -> {
                    logVal("serviceDto.serviceId", serviceDto.getServiceId());
                    logVal("serviceDto.dtVisit",    serviceDto.getDtVisit());
                    logVal("serviceDto.visitObjectId", serviceDto.getVisitObjectId());
                    ServiceEntity service = serviceRepository.findById(serviceDto.getServiceId())
                            .orElseThrow(() -> new EntityNotFoundException
                                    (String.format("Service не найден: %s", serviceDto.getServiceId())));

                    if (Boolean.TRUE.equals(service.getIsNeedVisitDate()) && serviceDto.getDtVisit() == null) {
                        throw new IllegalArgumentException(
                                String.format("dtVisit обязателен для услуги с ID %s", serviceDto.getServiceId()));
                    }

                    if ((serviceDto.getVisitObjectId() == null || serviceDto.getVisitObjectId().isEmpty()) && service.getIsVisitObjectUseForCost()) {
                        throw new IllegalArgumentException(String.format("VisitObjectId обязатен для услуги ID: %s", service.getId()));
                    }

                    if ((serviceDto.getCategoryVisitor() == null || serviceDto.getCategoryVisitor().isEmpty()) && service.getIsVisitorCountUseForCost()) {
                        throw new IllegalArgumentException(String.format("CategoryVisitor обязатен для услуги ID: %s", service.getId()));
                    }

                    Double cost = serviceDto.getServiceCost() != null ? serviceDto.getServiceCost() : 0;
                    OrderServiceEntity orderService = orderServiceMapper.toEntity(serviceDto);
                    orderService.setService(service);
                    orderService.setOrder(order);
                    orderService.setCost(cost);
                    orderService.setServiceCount(serviceDto.getServiceCount());
                    orderService.setServiceStateId(ServiceState.ORDERED.getCode());
                    return orderService;
                })
                .toList();
    }



    @Transactional
    public OrderCreateResponseDto createEditableOrder(EditableOrderRequestDto requestDto) {
        log.debug(">>> createSimpleOrder()");
        logVal("requestDto", requestDto);
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        OrderEntityUtil.initialize(orderEntity);

        logVal("generated orderEntity.orderId", orderEntity.getOrderId());

        if (orderEntity.getOrderId() == null) {
            orderEntity.setOrderId(generateUniqueOrderId());
            logVal("generated orderId", orderEntity.getOrderId());
        }

        if (orderEntity.getOrderBarcode() == null || orderEntity.getOrderBarcode().isEmpty()) {
            orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateUniqueOrderBarcode(orderRepository));
            logVal("generated barcode", orderEntity.getOrderBarcode());
        }

        List<OrderServiceEntity> orderServiceEntities = buildEditableOrderServices(requestDto, orderEntity);
        orderEntity.setService(orderServiceEntities);

        logVal("orderServiceEntities.size", orderServiceEntities.size());
        if (orderRepository.existsByOrderBarcode(orderEntity.getOrderBarcode())) {
            throw new IllegalStateException("Штрихкод уже существует: " + orderEntity.getOrderBarcode());
        }

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        for (int i = 0; i < orderServiceEntities.size(); i++) {
            OrderServiceEntity orderServiceEntity = orderServiceEntities.get(i);
            EditableOrderServiceDto editableDto = requestDto.getService().get(i);

            if (editableDto.getVisitObjectId() != null) {
                for (Long voId : editableDto.getVisitObjectId()) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(voId)
                            .orElseThrow(() -> new EntityNotFoundException("VisitObject не найден: " + voId));
                    orderServiceVisitObjectRepository.save(OrderServiceVisitObjectEntity.builder()
                            .orderService(orderServiceEntity)
                            .visitObject(visitObject)
                            .build());
                }
            }

            if (editableDto.getCategoryVisitor() != null) {
                for (CategoryVisitorCountDto catDto : editableDto.getCategoryVisitor()) {
                    CategoryVisitorEntity catEntity = categoryVisitorRepository.findById(catDto.getCategoryVisitorId())
                            .orElseThrow(() -> new EntityNotFoundException("CategoryVisitor не найден: " + catDto.getCategoryVisitorId()));
                    orderServiceVisitorRepository.save(OrderServiceVisitorEntity.builder()
                            .orderService(orderServiceEntity)
                            .categoryVisitor(catEntity)
                            .visitorCount(catDto.getVisitorCount())
                            .build());
                }
            }
        }

        List<SoldServiceDto> soldServices = orderServiceEntities.stream().map(os -> {
            SoldServiceDto dto = new SoldServiceDto();
            dto.setOrderServiceId(os.getId());
            dto.setServiceId(os.getService().getId());
            dto.setServiceCost(os.getCost());
            dto.setServiceCount(os.getServiceCount());
            dto.setDtVisit(os.getDtVisit());

            List<Long> visitObjectIds = visitObjectRepository.findByOrderServiceId(os.getId())
                    .stream().map(VisitObjectEntity::getId).toList();
            dto.setVisitObjectId(visitObjectIds);
            dto.setVisitObject(visitObjectIds);

            List<CategoryVisitorCountDto> visitors = orderServiceVisitorRepository.findByOrderServiceId(os.getId())
                    .stream()
                    .map(v -> new CategoryVisitorCountDto(v.getCategoryVisitor().getId(), v.getVisitorCount()))
                    .toList();
            dto.setCategoryVisitor(visitors);

            return dto;
        }).toList();
        logVal("soldServices.size", soldServices.size());

        SoldOrderRequestDto soldRequest = new SoldOrderRequestDto();
        soldRequest.setOrderId(savedOrder.getId());
        soldRequest.setService(soldServices);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_EDITABLE_ORDER")
                .description("Создан изменяемый заказ № " + savedOrder.getId())
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        OrderCreateResponseDto response = orderMapper.toResponseDto(savedOrder);
        response.setSoldRequest(soldRequest);
        logVal("OrderCreateResponseDto (out)", response);

        log.debug("Order {} persisted with barcode {}", savedOrder.getId(), savedOrder.getOrderBarcode());
        return response;
    }





    private List<OrderServiceEntity> buildEditableOrderServices(EditableOrderRequestDto dto, OrderEntity order) {
        log.debug("--- buildEditableOrderServices()");
        logVal("dto.service.size", dto.getService().size());
        return dto.getService().stream()
                .map(editableDto -> {
                    logVal("editableDto.serviceId", editableDto.getServiceId());
                    logVal("editableDto.dtVisit",   editableDto.getDtVisit());
                    logVal("editableDto.visitObjectId", editableDto.getVisitObjectId());
                    ServiceEntity service = serviceRepository.findById(editableDto.getServiceId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format("Service не найден: %s", editableDto.getServiceId())));

                    if (Boolean.TRUE.equals(service.getIsNeedVisitDate()) && editableDto.getDtVisit() == null) {
                        throw new IllegalArgumentException(
                                String.format("dtVisit обязателен для услуги с ID %s", editableDto.getServiceId()));
                    }

                    if ((editableDto.getVisitObjectId() == null || editableDto.getVisitObjectId().isEmpty()) && service.getIsVisitObjectUseForCost()) {
                        throw new IllegalArgumentException(String.format("VisitObjectId обязатен для услуги ID: %s", service.getId()));
                    }

                    if ((editableDto.getCategoryVisitor() == null || editableDto.getCategoryVisitor().isEmpty()) && service.getIsVisitorCountUseForCost()) {
                        throw new IllegalArgumentException(String.format("CategoryVisitor обязатен для услуги ID: %s", service.getId()));
                    }

                    OrderServiceEntity orderService = orderServiceMapper.toEntity(editableDto);
                    orderService.setService(service);
                    orderService.setOrder(order);
                    Double cost = editableDto.getServiceCost() != null ? editableDto.getServiceCost() : 0;
                    orderService.setCost(cost);
                    orderService.setServiceCount(editableDto.getServiceCount());
                    orderService.setServiceStateId(ServiceState.ORDERED.getCode());
                    return orderService;
                })
                .toList();
    }




    @Transactional
    public OrderDto cancelOrder(OrderCancelDto requestDto) {
        // находим заказ
        Long orderId = requestDto.getOrderId();

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Заказ не найден: %s", orderId)));

        List<Long> serviceId = requestDto.getOrderServiceId();

        if (serviceId == null || serviceId.isEmpty()) {
            // полная отмена
            // удаляем все услуги из order_services для данного заказа
            List<OrderServiceEntity> allServices = orderEntity.getService();
            List<Long> orderServiceIds = allServices.stream()
                    .map(OrderServiceEntity::getId)
                    .toList();
            orderServiceVisitorRepository.deleteByOrderServiceIdIn(orderServiceIds);
            orderServiceVisitObjectRepository.deleteByOrderServiceIdIn(orderServiceIds);
            orderServiceRepository.deleteAll(allServices);
            orderEntity.getService().clear();
        } else {
            // частичная отмена
            // удаляем только те услуги, чьи ID есть в orderServiceId
            List<OrderServiceEntity> toRemove = orderEntity.getService().stream()
                    .filter(os -> serviceId.contains(os.getId()))
                    .toList();

            if (toRemove.isEmpty()) {
                // ни одна услуга не совпала с тем, что прислали
                //можно бросить исключение
                throw new PartialCancellationException("Не найдено соответствующих услуг для частичной отмены");
            }
            List<Long> orderServiceIds = toRemove.stream()
                    .map(OrderServiceEntity::getId)
                    .toList();
            orderServiceVisitorRepository.deleteByOrderServiceIdIn(orderServiceIds);
            orderServiceVisitObjectRepository.deleteByOrderServiceIdIn(orderServiceIds);
            orderServiceRepository.deleteAll(toRemove);
            orderEntity.getService().removeAll(toRemove);
        }


        orderRepository.save(orderEntity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CANCEL_ORDER")
                .description(String.format("Отменён заказ № %s", orderId))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return orderMapper.toDto(orderEntity);

    }


    @Transactional
    public OrderDto refundOrder(OrderRefundDto dto) {
        Long orderId = dto.getOrderId();
        // Получаем заказ по ID
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Заказ не найден: %s", orderId)));

        List<Long> serviceIdsToRefund;

        if (dto.getServices() == null || dto.getServices().isEmpty()) {
            // Полная отмена - соберем все orderServiceId заказа
            List<OrderServiceEntity> allServices = orderServiceRepository.findAllByOrderId(orderId);
            serviceIdsToRefund = allServices.stream()
                    .map(OrderServiceEntity::getId)
                    .toList();
        } else {
            // частичная отмена - берем из запроса
            serviceIdsToRefund = dto.getServices().stream()
                    .map(OrderRefundServiceDto::getOrderServiceId)
                    .toList();

            // Валидация: проверим, что все услуги принадлежат заказу
            List<Long> existingIds;
            try (Stream<OrderServiceEntity> stream = orderServiceRepository.findAllByOrderId(orderId).stream()) {
                existingIds = stream.map(OrderServiceEntity::getId).toList();
            }

            boolean valid = serviceIdsToRefund.stream().allMatch(existingIds::contains);
            if (!valid) {
                throw new InvalidRefundRequestException
                        (String.format("Некоторые услуги не относятся к заказу: %s", orderId));
            }
        }

        // Если список на возврат пуст - ошибка
        if (serviceIdsToRefund.isEmpty()) {
            throw new EmptyRefundListException("Не найдено ни одной услуги для возврата");
        }

        // Обновляем состояние проданных услуг
        soldServiceRepository.markAsRefunded(serviceIdsToRefund);

        // Обновляем состояние услуг в самом заказе
        order.getService().stream()
                .filter(s -> serviceIdsToRefund.contains(s.getId()))
                .forEach(s -> s.setServiceStateId(ServiceState.RETURNED.getCode()));

        orderRepository.save(order);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("REFUND_ORDER")
                .description(String.format("Возврат услуг по заказу № %s", orderId))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        // вернем заказ в текущем состоянии (уже с измененными услугами)
        return orderMapper.toDto(order);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }

    private Integer generateUniqueOrderId() {
        Integer maxOrderId = orderRepository.findMaxOrderId();
        return maxOrderId + 1;
    }

    private static <T> void logVal(String name, T value) {
        log.debug("{} -> type: {}, value: {}", name,
                value == null ? "null" : value.getClass().getName(),
                value);
    }


}
