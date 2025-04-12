package com.ticket.terminal.dto;

/*
DTO для списка доступных экскурсий
Эндпоинт: GET /REST/Excursion
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcursionDto {

    private Long serviceId;
    private String serviceName;
    private boolean disableEditVisitObject;
    private boolean disableEditVisitor;
    private boolean visitObjectUseForCost;
    private boolean categoryVisitorUseForCost;
    private boolean visitorCountUseForCost;
    private boolean useOneCategory;
    private List<VisitObjectDto> visitObjects;
    private List<CategoryVisitorDto> categoryVisitors;
    private List<PriceDto> prices;
}
