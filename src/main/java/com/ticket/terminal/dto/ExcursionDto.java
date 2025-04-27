package com.ticket.terminal.dto;
/*
DTO для списка доступных экскурсий
Эндпоинт: GET /REST/Excursion
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ExcursionDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("DisableEditVisitObject")
    private Boolean disableEditVisitObject;

    @JsonProperty("DisableEditVisitor")
    private Boolean disableEditVisitor;

    @JsonProperty("VisitObjectUseForCost")
    private Boolean visitObjectUseForCost;

    @JsonProperty("CategoryVisitorUseForCost")
    private Boolean categoryVisitorUseForCost;

    @JsonProperty("VisitorCountUseForCost")
    private Boolean visitorCountUseForCost;

    @JsonProperty("UseOneCategory")
    private Boolean useOneCategory;

    @JsonProperty("VisitObjects")
    private List<VisitObjectDto> visitObjects;

    @JsonProperty("CategoryVisitors")
    private List<CategoryVisitorDto> categoryVisitors;

    @JsonProperty("Prices")
    private List<PriceDto> prices;
}
