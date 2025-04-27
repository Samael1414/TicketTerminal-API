package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
/*
DTO для возврата оплаты
Эндпоинт:

POST /REST/Order/Refund (Полный и частичный возврат)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderRefundServiceDto {

    @JsonProperty("OrderServiceId")
    private Long orderServiceId;

    @JsonProperty("Cost")
    private Integer cost;
}
