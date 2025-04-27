package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
/*
DTO для категорий посетителей (CategoryVisitorDto)
GET /REST/Service/Editable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CategoryVisitorDto {

    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;

    @JsonProperty("CategoryVisitorName")
    private String categoryVisitorName;

    @JsonProperty("RequireVisitorCount")
    private Integer requireVisitorCount;

    @JsonProperty("GroupCategoryVisitorId")
    private Long groupCategoryVisitorId;
}
