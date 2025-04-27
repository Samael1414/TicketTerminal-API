package com.ticket.terminal.service;

import com.ticket.terminal.dto.CategoryVisitorCountDto;
import com.ticket.terminal.dto.CostCalculationDto;
import com.ticket.terminal.dto.CostResponseDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.PriceEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.repository.PriceRepositoryCustom;
import com.ticket.terminal.repository.ServiceRepository;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCostService {

    private final PriceRepositoryCustom priceRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;

    public CostResponseDto calculateCost(CostCalculationDto dto) {
        int totalCost = 0;

        ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException
                        (String.format("Service не найден: %s", dto.getServiceId())));

        boolean useVisitObject = Boolean.TRUE.equals(service.getIsVisitObjectUseForCost());
        boolean useCategory = Boolean.TRUE.equals(service.getIsCategoryVisitorUseForCost());
        boolean useVisitorCount = Boolean.TRUE.equals(service.getIsVisitorCountUseForCost());

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
        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CALCULATE_COST")
                .description(String.format("Рассчитана стоймость для услуги ID: %d. Итог: %d",
                        dto.getServiceId(), totalCost))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());
        return new CostResponseDto(totalCost);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
