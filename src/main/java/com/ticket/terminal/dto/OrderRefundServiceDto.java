package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
DTO для возврата оплаты
Эндпоинт:

POST /REST/Order/Refund (Полный и частичный возврат)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRefundServiceDto {

    private Long orderServiceId;
    private Integer cost;
}
