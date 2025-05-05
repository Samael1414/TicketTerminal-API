/**
 * DTO для передачи ответа с информацией о заказах (Order Response).
 * 
 * Назначение:
 * - Используется для возврата списка заказов или информации о заказах по фильтру (например, по диапазону дат).
 * - Применяется в контроллерах и сервисах.
 * 
 * Содержит данные:
 * - список заказов, агрегированные данные, статусы, сообщения и др.
 */
package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderResponseDto {

    @JsonProperty("Order")
    private List<OrderDto> order;
}
