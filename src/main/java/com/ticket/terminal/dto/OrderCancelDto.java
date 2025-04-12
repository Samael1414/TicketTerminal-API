package com.ticket.terminal.dto;

/*
DTO для отмены заказа
Эндпоинт:

POST /REST/Order/Cancel (Полная и частичная отмена)
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelDto {

    private Long orderId;
    private List<Long> orderServiceId;
}
