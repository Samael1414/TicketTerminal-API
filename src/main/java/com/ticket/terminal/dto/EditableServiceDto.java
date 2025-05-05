/**
 * DTO для описания редактируемой услуги (Editable Service).
 * 
 * Назначение:
 * - Используется для передачи информации о редактируемых услугах между слоями приложения и внешними системами.
 * - Применяется в контроллерах, сервисах, клиентах и для обмена с внешними API.
 * 
 * Содержит данные:
 * - id услуги, параметры редактирования, стоимость, ограничения и др.
 */
package com.ticket.terminal.dto;
/*
# Получение списка редактируемых услуг, доступных для продажи
---
## GET /REST/Service/Editable

 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EditableServiceDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Comment")
    private String comment;

    @JsonProperty("IsDisableEditVisitObject")
    private Boolean isDisableEditVisitObject;

    @JsonProperty("IsDisableEditVisitor")
    private Boolean isDisableEditVisitor;

    @JsonProperty("IsVisitObjectUseForCost")
    private Boolean isVisitObjectUseForCost;

    @JsonProperty("IsCategoryVisitorUseForCost")
    private Boolean isCategoryVisitorUseForCost;

    @JsonProperty("IsVisitorCountUseForCost")
    private Boolean isVisitorCountUseForCost;

    @JsonProperty("IsUseOneCategory")
    private Boolean isUseOneCategory;

    @JsonProperty("MaxVisitObjectCount")
    private Integer maxVisitObjectCount;

    @JsonProperty("MaxVisitorCount")
    private Integer maxVisitorCount;

    @JsonProperty("ActiveKindId")
    private Integer activeKindId;

    @JsonProperty("IsNeedVisitDate")
    private Boolean isNeedVisitDate;

    @JsonProperty("IsNeedVisitTime")
    private Boolean isNeedVisitTime;

    @JsonProperty("ActiveDays")
    private Integer activeDays;

    @JsonProperty("dtBegin")
    private String dtBegin;

    @JsonProperty("dtEnd")
    private String dtEnd;

    @JsonProperty("Dates")
    private List<OffsetDateTime> dates;

    @JsonProperty("ProCultureIdentifier")
    private Integer proCultureIdentifier;

    @JsonProperty("ProCultureChecked")
    private Boolean proCultureChecked;

    @JsonProperty("PaymentKindId")
    private List<Integer> paymentKindId;

    @JsonProperty("VisitObject")
    private List<VisitObjectDto> visitObjects;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorDto> categoryVisitor;

    @JsonProperty("Price")
    private List<PriceDto> prices;

    @JsonProperty("SeanceGrid")
    private List<SeanceGridDto> seanceGrid;
}
