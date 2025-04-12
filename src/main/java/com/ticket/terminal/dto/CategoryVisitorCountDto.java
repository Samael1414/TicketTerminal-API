package com.ticket.terminal.dto;

/*
DTO для передачи количества посетителей по категориям (используется в расчете цены)
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
public class CategoryVisitorCountDto {

    private Long categoryVisitorId;
    private Long visitorCount;
}
