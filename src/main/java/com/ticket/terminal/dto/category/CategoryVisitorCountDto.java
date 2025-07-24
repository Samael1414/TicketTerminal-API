package com.ticket.terminal.dto.category;
/*
DTO для передачи количества посетителей по категориям (используется в расчете цены)
Эндпоинт: POST /REST/Order/Cost
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CategoryVisitorCountDto {

    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;

    @JsonProperty("VisitorCount")
    private Long visitorCount;

    @JsonProperty("CategoryVisitorName")
    private String categoryVisitorName;
}
