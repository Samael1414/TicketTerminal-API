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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SoldRequestEnricherService {

    private final OrderServiceRepository orderServiceRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final OrderServiceVisitorRepository orderServiceVisitorRepository;

    /**
     * Заполняет ServiceId, Cost, Count, VisitObjectId, CategoryVisitor, DtVisit.
     */
    public void enrich(SoldOrderRequestDto req) {

        req.getService().forEach(it -> {
            Long osId = it.getOrderServiceId();
            log.debug("enrich [{}] before -> {}", osId, it);
            if (osId == null) {
                throw new IllegalArgumentException("OrderServiceId is null");
            }

            OrderServiceEntity os = orderServiceRepository
                    .findWithServiceById(osId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("OrderService not found " + osId));

            // 1. ServiceId
            if (it.getServiceId() == null) {
                it.setServiceId(os.getService().getId());
            }

            // 2. Cost / Count
            if (it.getServiceCost()  == null) it.setServiceCost(os.getCost());
            if (it.getServiceCount() == null) it.setServiceCount(os.getServiceCount());

            // 3. DtVisit
            if (it.getDtVisit() == null) it.setDtVisit(os.getDtVisit());

            // 4. VisitObjectId
            if (it.getVisitObjectId() == null || it.getVisitObjectId().isEmpty()) {
                List<Long> voIds = visitObjectRepository
                        .findByOrderServiceId(osId)
                        .stream()
                        .map(VisitObjectEntity::getId)
                        .toList();
                it.setVisitObjectId(voIds);
            }

            // 5. CategoryVisitor
            if (it.getCategoryVisitor() == null || it.getCategoryVisitor().isEmpty()) {
                var visitors = orderServiceVisitorRepository
                        .findByOrderServiceId(osId)
                        .stream()
                        .map(v -> new CategoryVisitorCountDto(
                                v.getCategoryVisitor().getId(),
                                v.getVisitorCount()))
                        .toList();
                it.setCategoryVisitor(visitors);
                log.debug("enrich [{}] after  -> {}", osId, it);
            }
        });
    }
}
