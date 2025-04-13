package com.ticket.terminal.dto;
/*
DTO для расчета стоимости (ответ сервера)
Эндпоинт: POST /REST/Order/Cost
 */
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostResponseDto {

    private Integer cost;
}
