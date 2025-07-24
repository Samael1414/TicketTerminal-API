/**
 * DTO для передачи данных о возврате заказа (Order Refund).
 * 
 * Назначение:
 * - Используется для передачи параметров возврата заказа между клиентом и backend.
 * - Применяется в контроллерах и сервисах для обработки возвратов.
 * 
 * Содержит данные:
 * - id заказа, сумма возврата, причина возврата и др.
 */
package com.ticket.terminal.dto.order;
/*
DTO для возврата оплаты
Эндпоинт:

POST /REST/Order/Refund (Полный и частичный возврат)
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
public class OrderRefundDto {

    @JsonProperty("OrderId")
    private Long orderId;

    @JsonProperty("Services")
    private List<OrderRefundServiceDto> services;
}
