package com.ticket.terminal.service;

import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import com.ticket.terminal.dto.cost.CostCalculationDto;
import com.ticket.terminal.dto.cost.CostResponseDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.PriceEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.repository.PriceRepositoryCustom;
import com.ticket.terminal.repository.ServiceRepository;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderCostService {

    private final PriceRepositoryCustom priceRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;

    @Transactional
    public CostResponseDto calculateCost(CostCalculationDto dto) {
        ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Service не найден: " + dto.getServiceId()));

        boolean useVisitObject = Boolean.TRUE.equals(service.getIsVisitObjectUseForCost());
        boolean useCategory = Boolean.TRUE.equals(service.getIsCategoryVisitorUseForCost());
        boolean useVisitorCount = Boolean.TRUE.equals(service.getIsVisitorCountUseForCost());

        List<CategoryVisitorCountDto> visitors = Optional.ofNullable(dto.getCategoryVisitor())
                .filter(list -> !list.isEmpty())
                .orElseGet(() -> List.of(new CategoryVisitorCountDto()));

        double totalCost = useVisitObject
                ? calculateWithVisitObjects(dto.getVisitObjectId(), visitors, service.getId(), useCategory, useVisitorCount)
                : calculateWithoutVisitObjects(visitors, service.getId(), useCategory, useVisitorCount);

        logCostCalculation(dto.getServiceId(), totalCost);
        return new CostResponseDto(totalCost);
    }

    private double calculateWithVisitObjects(
            List<Long> visitObjectIds,
            List<CategoryVisitorCountDto> visitors,
            Long serviceId,
            boolean useCategory,
            boolean useVisitorCount
    ) {
        if (visitObjectIds == null || visitObjectIds.isEmpty()) {
            return 0.0;
        }

        return visitors.stream()
                .flatMap(visitorCountDto -> visitObjectIds.stream()
                        .map(visitorId -> new AbstractMap.SimpleEntry<>(visitorCountDto, visitorId)))
                .mapToDouble(entry -> {
                    CategoryVisitorCountDto categoryVisitorCountDto = entry.getKey();
                    Long entryValue = entry.getValue();
                    double cost = findCost(serviceId,
                            entryValue,
                            useCategory ? categoryVisitorCountDto.getCategoryVisitorId() : null);
                    int numVisitors = useVisitorCount && categoryVisitorCountDto.getVisitorCount() != null
                            ? categoryVisitorCountDto.getVisitorCount().intValue()
                            : 1;
                    return cost * numVisitors;
                })
                .sum();
    }

    private double calculateWithoutVisitObjects(
            List<CategoryVisitorCountDto> visitors,
            Long serviceId,
            boolean useCategory,
            boolean useVisitorCount
    ) {
        return visitors.stream()
                .mapToDouble(categoryVisitorCountDto -> {
                    double cost = findCost(serviceId, null,
                            useCategory ? categoryVisitorCountDto.getCategoryVisitorId() : null);
                    int numVisitors = useVisitorCount && categoryVisitorCountDto.getVisitorCount() != null
                            ? categoryVisitorCountDto.getVisitorCount().intValue()
                            : 1;
                    return cost * numVisitors;
                })
                .sum();
    }

    /**
     * Пытается найти цену по конкретному visitObjectId, а если не нашлось —
     * по «общей» записи (visitObjectId = null). Возвращает 0.0, если и того нет.
     */
    private double findCost(Long serviceId,
                            Long visitObjectId,
                            Long categoryVisitorId) {
        // первое попытка получить цену по конкретному visitObjectId
        Optional<Double> optional = priceRepository
                .findMatchingPrice(serviceId, visitObjectId, categoryVisitorId)
                .map(PriceEntity::getCost);

        if (optional.isPresent()) {
            return optional.get();
        }
        // fallback: ищем «общую» цену (visitObjectId = null)
        return priceRepository
                .findMatchingPrice(serviceId, null, categoryVisitorId)
                .map(PriceEntity::getCost)
                .orElse(0.0);
    }


    private void logCostCalculation(Long serviceId, double totalCost) {
        UsersEntity current = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(current)
                .actionType("CALCULATE_COST")
                .description(String.format("Рассчитана стоимость для услуги ID: %d. Итог: %.2f",
                        serviceId, totalCost))
                .createdAt(LocalDateTime.now())
                .actorName(current.getUserName())
                .build());
    }

    private UsersEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("Текущий пользователь не найден"));
    }
}

