package com.ticket.terminal.dto;

import lombok.*;
/*
DTO для возврата оплаты
Эндпоинт:

POST /REST/Order/Refund (Полный и частичный возврат)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundServiceDto {

    private Long orderServiceId;
    private Integer cost;
}
