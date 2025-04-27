package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TLMuseumServiceResponseDto {

    private List<MuseumServiceItemDto> service;
    private List<VisitObjectItemDto> visitObject;
    private List<CategoryVisitorDto> categoryVisitor;
    private List<SeanceGridDto> seanceGrid;
}
