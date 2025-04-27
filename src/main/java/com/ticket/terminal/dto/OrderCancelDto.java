package com.ticket.terminal.dto;
/*
DTO для отмены заказа
Эндпоинт:

POST /REST/Order/Cancel (Полная и частичная отмена)
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderCancelDto {

    @JsonProperty("OrderId")
    private Long orderId;

    @JsonProperty("OrderServiceId")
    private List<Long> orderServiceId;
}
