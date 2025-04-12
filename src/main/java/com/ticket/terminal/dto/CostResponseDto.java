package com.ticket.terminal.dto;

/*
DTO для расчета стоимости (ответ сервера)
Эндпоинт: POST /REST/Order/Cost
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostResponseDto {

    private Integer cost;
}
