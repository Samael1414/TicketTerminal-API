package com.ticket.terminal.dto;
/*
DTO для списка доступных экскурсий
Эндпоинт: GET /REST/Excursion
 */
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionDto {

    private Long serviceId;
    private String serviceName;
    private Boolean disableEditVisitObject;
    private Boolean disableEditVisitor;
    private Boolean visitObjectUseForCost;
    private Boolean categoryVisitorUseForCost;
    private Boolean visitorCountUseForCost;
    private Boolean useOneCategory;
    private List<VisitObjectDto> visitObjects;
    private List<CategoryVisitorDto> categoryVisitors;
    private List<PriceDto> prices;
}
