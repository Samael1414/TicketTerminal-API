package com.ticket.terminal.service;

import com.ticket.terminal.dto.CategoryVisitorCountDto;
import com.ticket.terminal.dto.SoldOrderRequestDto;
import com.ticket.terminal.entity.OrderServiceEntity;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.repository.OrderServiceRepository;
import com.ticket.terminal.repository.OrderServiceVisitorRepository;
import com.ticket.terminal.repository.VisitObjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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

        requestDto.getService().forEach(dto -> {
            Long serviceId = dto.getOrderServiceId();
            if (serviceId == null) {
                throw new IllegalArgumentException("OrderServiceId is null");
            }

            OrderServiceEntity serviceEntity = orderServiceRepository
                    .findWithServiceById(serviceId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("OrderService not found " + serviceId));

            // 1. ServiceId
            if (dto.getServiceId() == null) {
                dto.setServiceId(serviceEntity.getService().getId());
            }

            // 2. Cost / Count
            if (dto.getServiceCost()  == null) dto.setServiceCost(serviceEntity.getCost());
            if (dto.getServiceCount() == null) dto.setServiceCount(serviceEntity.getServiceCount());

            // 3. DtVisit
            if (dto.getDtVisit() == null) dto.setDtVisit(serviceEntity.getDtVisit());

            // 4. VisitObjectId
            if (dto.getVisitObjectId() == null || dto.getVisitObjectId().isEmpty()) {
                List<Long> visitObjectIds = visitObjectRepository
                        .findByOrderServiceId(serviceId)
                        .stream()
                        .map(VisitObjectEntity::getId)
                        .toList();
                dto.setVisitObjectId(visitObjectIds);
            }

            // 5. CategoryVisitor
            if (dto.getCategoryVisitor() == null || dto.getCategoryVisitor().isEmpty()) {
                var visitors = orderServiceVisitorRepository
                        .findByOrderServiceId(serviceId)
                        .stream()
                        .map(visitor -> new CategoryVisitorCountDto(
                                visitor.getCategoryVisitor().getId(),
                                visitor.getVisitorCount()))
                        .toList();
                dto.setCategoryVisitor(visitors);
            }
        });
    }
}
