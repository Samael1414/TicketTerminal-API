/**
 * DTO для передачи данных об отмене заказа (Order Cancel).
 * 
 * Назначение:
 * - Используется для передачи параметров отмены заказа между клиентом и backend.
 * - Применяется в контроллерах и сервисах для обработки отмены заказа.
 * 
 * Содержит данные:
 * - id заказа, причина отмены, инициатор отмены и др.
 */
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
