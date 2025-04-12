package com.ticket.terminal.service;


import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.mapper.OrderMapper;
import com.ticket.terminal.mapper.OrderServiceMapper;
import com.ticket.terminal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
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
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(entity);
    }

    public OrderResponseDto getOrderByDateRange(LocalDateTime dtBegin, LocalDateTime dtEnd) {
        List<OrderEntity> orders = orderRepository.findAllByCreatedAtBetween(dtBegin, dtEnd);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .toList();
        return new OrderResponseDto(orderDtos);
    }

    @Transactional
    public OrderCreateResponseDto createSimpleOrder(SimpleOrderRequestDto requestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setOrderStateId(ServiceState.ORDERED.getCode());

        List<OrderServiceEntity> orderServiceEntities = buildOrderServices(requestDto, orderEntity);
        orderEntity.setService(orderServiceEntities);

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "CREATE_ORDER",
                String.format("Создан заказ № %s", savedOrder.getId()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));
        return orderMapper.toResponseDto(savedOrder);
    }

    private List<OrderServiceEntity> buildOrderServices(SimpleOrderRequestDto dto, OrderEntity order) {
        return dto.getService().stream()
                .map(serviceDto -> {
                    ServiceEntity service = serviceRepository.findById(serviceDto.getServiceId())
                            .orElseThrow(() -> new EntityNotFoundException
                                    (String.format("Service not found: %s", serviceDto.getServiceId())));

                    Integer cost = serviceDto.getServiceCost() != null ? serviceDto.getServiceCost() : 0;
                    OrderServiceEntity orderService = orderServiceMapper.toEntity(serviceDto);
                    orderService.setService(service);
                    orderService.setOrder(order);
                    orderService.setCost(cost);
                    orderService.setServiceStateId(ServiceState.ORDERED.getCode());
                    return orderService;
                })
                .toList();
    }


    @Transactional
    public OrderCreateResponseDto createEditableOrder(EditableOrderRequestDto requestDto) {
        // Преобразуем DTO в OrderEntity (с автозаполнением barcode и createdAt)
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setOrderStateId(ServiceState.ORDERED.getCode());

        // Обрабатываем каждую позицию заказа через EditableOrderServiceDto
        List<OrderServiceEntity> orderServiceEntities = buildEditableOrderServices(requestDto, orderEntity);
        orderEntity.setService(orderServiceEntities);

        // Сохраняем заказ (cascade = ALL сохранит позиции заказа)
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // Для каждой позиции заказа сохраняем дополнительные данные в промежуточные таблицы:
        for (int i = 0; i < orderServiceEntities.size(); i++) {
            OrderServiceEntity orderServiceEntity = orderServiceEntities.get(i);
            // Получаем соответствующий EditableOrderServiceDto
            EditableOrderServiceDto editableDto = requestDto.getService().get(i);

            // Сохраняем выбранные объекты посещения
            if (editableDto.getVisitObjectId() != null) {
                for (Long voId : editableDto.getVisitObjectId()) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(voId)
                            .orElseThrow(() -> new EntityNotFoundException
                                    (String.format("VisitObject not found: %s", voId)));

                    OrderServiceVisitObjectEntity orderServiceVisitObjectEntity = new OrderServiceVisitObjectEntity();
                    orderServiceVisitObjectEntity.setOrderService(orderServiceEntity);
                    orderServiceVisitObjectEntity.setVisitObject(visitObject);
                    orderServiceVisitObjectRepository.save(orderServiceVisitObjectEntity);
                }
            }

            // Сохраняем выбранные категории посетителей с количеством
            if (editableDto.getCategoryVisitor() != null) {
                for (CategoryVisitorCountDto catDto : editableDto.getCategoryVisitor()) {
                    CategoryVisitorEntity catEntity = categoryVisitorRepository.findById(catDto.getCategoryVisitorId())
                            .orElseThrow(() -> new EntityNotFoundException
                                    (String.format("CategoryVisitor not found: %s", catDto.getCategoryVisitorId())));

                    OrderServiceVisitorEntity osvVisitor = new OrderServiceVisitorEntity();
                    osvVisitor.setOrderService(orderServiceEntity);
                    osvVisitor.setCategoryVisitor(catEntity);
                    osvVisitor.setVisitorCount(catDto.getVisitorCount());
                    orderServiceVisitorRepository.save(osvVisitor);
                }
            }
        }
        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "CREATE_EDITABLE_ORDER",
                String.format("Создан изменяемый заказ № %s", savedOrder.getId()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        //  Формируем и возвращаем ответ
        return orderMapper.toResponseDto(savedOrder);
    }

    private List<OrderServiceEntity> buildEditableOrderServices(EditableOrderRequestDto dto, OrderEntity order) {
        return dto.getService().stream()
                .map(editableDto -> {
                    ServiceEntity service = serviceRepository.findById(editableDto.getServiceId())
                            .orElseThrow(() -> new EntityNotFoundException
                                    (String.format("Service not found: %s", editableDto.getServiceId())));

                    // Маппинг EditableOrderServiceDto в OrderServiceEntity
                    OrderServiceEntity orderService = orderServiceMapper.toEntity(editableDto);
                    orderService.setService(service);
                    orderService.setOrder(order);
                    Integer cost = editableDto.getServiceCost() != null ? editableDto.getServiceCost() : 0;
                    orderService.setCost(cost);
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
                        (String.format("Order not found: %s", orderId)));

        List<Long> serviceId = requestDto.getOrderServiceId();

        if (serviceId == null || serviceId.isEmpty()) {
            // полная отмена
            // удаляем все услуги из order_services для данного заказа
            List<OrderServiceEntity> allServices = orderEntity.getService();
            List<Long> ids = allServices.stream()
                    .map(OrderServiceEntity::getId)
                    .toList();
            orderServiceVisitorRepository.deleteByOrderServiceIdIn(ids);
            orderServiceVisitObjectRepository.deleteByOrderServiceIdIn(ids);
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
                throw new IllegalStateException("No matching services found for partial cancel");
            }

            List<Long> ids = toRemove.stream()
                    .map(OrderServiceEntity::getId)
                    .toList();
            orderServiceVisitorRepository.deleteByOrderServiceIdIn(ids);
            orderServiceVisitObjectRepository.deleteByOrderServiceIdIn(ids);
            orderServiceRepository.deleteAll(toRemove);
            orderEntity.getService().removeAll(toRemove);
        }


        orderRepository.save(orderEntity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "CANCEL_ORDER",
                String.format("Отменён заказ № %s", orderId),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        return orderMapper.toDto(orderEntity);

    }


    @Transactional
    public OrderDto refundOrder(OrderRefundDto dto) {
        Long orderId = dto.getOrderId();
        // Получаем заказ по ID
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Order not found: %s", orderId)));

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
            List<Long> existingIds = orderServiceRepository.findAllByOrderId(orderId).stream()
                    .map(OrderServiceEntity::getId)
                    .toList();

            boolean valid = serviceIdsToRefund.stream()
                    .allMatch(existingIds::contains);
            if (!valid) {
                throw new IllegalArgumentException
                        (String.format("Some services do not belong to order: %s", orderId));
            }
        }

        // Если список на возврат пуст - ошибка
        if (serviceIdsToRefund.isEmpty()) {
            throw new IllegalStateException("No services found for refund");
        }

        // Обновляем состояние проданных услуг
        soldServiceRepository.markAsRefunded(serviceIdsToRefund);

        // Обновляем состояние услуг в самом заказе
        order.getService().stream()
                .filter(s -> serviceIdsToRefund.contains(s.getId()))
                .forEach(s -> s.setServiceStateId(ServiceState.RETURNED.getCode()));

        orderRepository.save(order);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "REFUND_ORDER",
                String.format("Возврат услуг по заказу № %s", orderId),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        // вернем заказ в текущем состоянии (уже с измененными услугами)
        return orderMapper.toDto(order);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
    }


}
