package com.ticket.terminal.dto;
/*
DTO для передачи количества посетителей по категориям (используется в расчете цены)
Эндпоинт: POST /REST/Order/Cost
 */
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVisitorCountDto {

    private Long categoryVisitorId;
    private Long visitorCount;
}
