package com.ticket.terminal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ServiceCreateDto {

    private String serviceName;
    private String description;
    private Double cost;
    private Long activeKindId;
    private Boolean isNeedVisitDate;
    private Boolean isNeedVisitTime;
    private LocalDateTime dtBegin;
    private LocalDateTime dtEnd;
    private Integer proCultureIdentifier;
    private Boolean isPROCultureChecked;
    private Boolean isDisableEditVisitObject;
    private Boolean isDisableEditVisitor;
    private Boolean isVisitObjectUseForCost;
    private Boolean isCategoryVisitorUseForCost;
    private Boolean isVisitorCountUseForCost;
    private Boolean isUseOneCategory;
}
