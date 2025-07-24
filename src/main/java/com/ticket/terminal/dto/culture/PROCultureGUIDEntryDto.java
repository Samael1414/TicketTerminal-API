package com.ticket.terminal.dto.culture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class PROCultureGUIDEntryDto {

    @JsonProperty("OrderId")
    private Integer orderId;

    @JsonProperty("SeatId")
    private Integer seatId;

    @JsonProperty("OrderServiceId")
    private Integer orderServiceId;

    @JsonProperty("ProCultureGUID")
    private String proCultureGUID;
}
