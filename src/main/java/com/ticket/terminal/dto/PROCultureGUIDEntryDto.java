package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PROCultureGUIDEntryDto {

    private Integer orderId;
    private Integer seatId;
    private Integer orderServiceId;
    private String proCultureGUID;
}
