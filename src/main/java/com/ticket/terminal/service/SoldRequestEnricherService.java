package com.ticket.terminal.service;

import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import com.ticket.terminal.dto.sold.SoldOrderRequestDto;
import com.ticket.terminal.dto.sold.SoldServiceDto;
import com.ticket.terminal.entity.order.OrderServiceEntity;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.repository.order.OrderServiceRepository;
import com.ticket.terminal.repository.order.OrderServiceVisitorRepository;
import com.ticket.terminal.repository.VisitObjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SoldRequestEnricherService {

    private final OrderServiceRepository orderServiceRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final OrderServiceVisitorRepository orderServiceVisitorRepository;

    /**
     * Заполняет ServiceId, Cost, Count, VisitObjectId, CategoryVisitor, DtVisit.
     */
    public void enrich(SoldOrderRequestDto requestDto) {
        requestDto.getService().forEach(this::enrichSingle);
    }

    private void enrichSingle(SoldServiceDto dto) {
        // 1) загружаем OrderServiceEntity (или кидаем ошибку)
        Long orderServiceIdIsNull = Optional.ofNullable(dto.getOrderServiceId())
                .orElseThrow(() -> new IllegalArgumentException("OrderServiceId is null"));
        OrderServiceEntity entity = orderServiceRepository
                .findWithServiceById(orderServiceIdIsNull)
                .orElseThrow(() ->
                        new EntityNotFoundException("OrderService not found: " + orderServiceIdIsNull)
                );

        // 2) Примитивы (id, cost, count, visit date)
        defaultIfNull(dto::getServiceId, dto::setServiceId, () -> entity.getService().getId());
        defaultIfNull(dto::getServiceCost, dto::setServiceCost, entity::getCost);
        defaultIfNull(dto::getServiceCount, dto::setServiceCount, entity::getServiceCount);
        defaultIfNull(dto::getDtVisit, dto::setDtVisit, entity::getDtVisit);

        // 3) Списки (visitObjectId, categoryVisitor)
        defaultIfEmpty(
                dto::getVisitObjectId,
                dto::setVisitObjectId,
                () -> visitObjectRepository
                        .findByOrderServiceId(orderServiceIdIsNull)
                        .stream()
                        .map(VisitObjectEntity::getId)
                        .toList()
        );

        defaultIfEmpty(
                dto::getCategoryVisitor,
                dto::setCategoryVisitor,
                () -> orderServiceVisitorRepository
                        .findByOrderServiceId(orderServiceIdIsNull)
                        .stream()
                        .map(visitorEntity -> new CategoryVisitorCountDto(
                                visitorEntity.getCategoryVisitor().getId(),
                                visitorEntity.getVisitorCount(),
                                visitorEntity.getCategoryVisitorName()))
                        .toList()
        );
    }

    /**
     * Если getter возвращает null, подставляем defaultSupplier.get()
     */
    private <T> void defaultIfNull(
            Supplier<T> getter,
            Consumer<T> setter,
            Supplier<T> defaultSupplier
    ) {
        if (getter.get() == null) {
            setter.accept(defaultSupplier.get());
        }
    }

    /**
     * Если getter возвращает null или пустой список, подставляем defaultSupplier.get()
     */
    private <T> void defaultIfEmpty(
            Supplier<List<T>> getter,
            Consumer<List<T>> setter,
            Supplier<List<T>> defaultSupplier
    ) {
        List<T> list = getter.get();
        if (list == null || list.isEmpty()) {
            setter.accept(defaultSupplier.get());
        }
    }
}
