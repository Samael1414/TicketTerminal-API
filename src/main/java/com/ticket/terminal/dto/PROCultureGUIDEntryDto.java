package com.ticket.terminal.dto;

import lombok.*;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PROCultureGUIDEntryDto {

    private Integer orderId;
    private Integer seatId;
    private Integer orderServiceId;
    private String proCultureGUID;
}
