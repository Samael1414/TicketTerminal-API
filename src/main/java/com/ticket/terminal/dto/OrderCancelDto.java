package com.ticket.terminal.dto;
/*
DTO для отмены заказа
Эндпоинт:

POST /REST/Order/Cancel (Полная и частичная отмена)
 */
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDto {

    private Long orderId;
    private List<Long> orderServiceId;
}
