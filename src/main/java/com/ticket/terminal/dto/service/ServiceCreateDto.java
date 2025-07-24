package com.ticket.terminal.dto.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ticket.terminal.dto.PriceDto;
import com.ticket.terminal.dto.visit.VisitObjectDto;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

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
    List<VisitObjectDto> visitObject;
    List<CategoryVisitorDto> categoryVisitor;
    List<PriceDto> price;
}
