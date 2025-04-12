package com.ticket.terminal.service;


import com.ticket.terminal.dto.CategoryVisitorCountDto;
import com.ticket.terminal.dto.CostCalculationDto;
import com.ticket.terminal.dto.CostResponseDto;
import com.ticket.terminal.entity.PriceEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.repository.PriceRepositoryCustom;
import com.ticket.terminal.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCostService {

    private final PriceRepositoryCustom priceRepository;
    private final ServiceRepository serviceRepository;

    public CostResponseDto calculateCost(CostCalculationDto dto) {
        int totalCost = 0;

        ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException
                        (String.format("Service not found %s", dto.getServiceId())));

        boolean useVisitObject = Boolean.TRUE.equals(service.getVisitObjectUseForCost());
        boolean useCategory = Boolean.TRUE.equals(service.getCategoryVisitorUseForCost());
        boolean useVisitorCount = Boolean.TRUE.equals(service.getVisitorCountUseForCost());

        List<CategoryVisitorCountDto> visitors =
                dto.getCategoryVisitor() != null ? dto.getCategoryVisitor() : List.of();

        if (useVisitObject) {
            for (CategoryVisitorCountDto category : visitors.isEmpty() ? List.of(new CategoryVisitorCountDto()) : visitors) {
                for (Long visitObjectId : dto.getVisitObjectId()) {
                    Integer cost = priceRepository.findMatchingPrice(
                                    service.getId(),
                                    visitObjectId,
                                    useCategory ? category.getCategoryVisitorId() : null
                            )
                            .map(PriceEntity::getCost)
                            .orElseGet(() -> priceRepository.findMatchingPrice(
                                            service.getId(),
                                            null,
                                            useCategory ? category.getCategoryVisitorId() : null).
                                    map(PriceEntity::getCost)
                                    .orElse(0));

                    int quantity = useVisitorCount ? category.getVisitorCount().intValue() : 1;
                    totalCost += cost * quantity;
                }
            }
        } else {
            for (CategoryVisitorCountDto category : visitors.isEmpty() ? List.of(new CategoryVisitorCountDto()) : visitors) {
                Integer cost = priceRepository.findMatchingPrice(
                                service.getId(),
                                null,
                                useCategory ? category.getCategoryVisitorId() : null
                        )
                        .map(PriceEntity::getCost)
                        .orElse(0);

                Long count = category.getVisitorCount();
                int quantity = useVisitorCount && count != null ? count.intValue() : 1;
                totalCost += cost * quantity;
            }
        }
        return new CostResponseDto(totalCost);
    }
}
