package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
DTO для цен (PriceDto)
GET /REST/Service/Editable
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {

    private Integer visitObjectId;
    private Integer categoryVisitorId;
    private Integer cost;
}
