package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/*
DTO для цен (PriceDto)
GET /REST/Service/Editable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class PriceDto {

    @JsonProperty("VisitObjectId")
    private Long visitObjectId;

    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;

    @JsonProperty("Cost")
    private Double cost;

    @JsonProperty("ServiceId")
    private Long serviceId;
}
