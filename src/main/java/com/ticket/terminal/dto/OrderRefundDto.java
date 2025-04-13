package com.ticket.terminal.dto;
/*
DTO для возврата оплаты
Эндпоинт:

POST /REST/Order/Refund (Полный и частичный возврат)
 */

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundDto {

    private Long orderId;
    private List<OrderRefundServiceDto> services;
}
