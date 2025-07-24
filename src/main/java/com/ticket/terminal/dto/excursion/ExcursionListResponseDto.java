package com.ticket.terminal.dto.excursion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ticket.terminal.dto.visit.VisitObjectDto;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ExcursionListResponseDto {

    @JsonProperty("Service")
    private List<ExcursionDto> service;

    @JsonProperty("VisitObject")
    private List<VisitObjectDto> visitObject;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorDto> categoryVisitor;
}
