package com.ticket.terminal.dto;

import lombok.*;

/*
DTO для цен (PriceDto)
GET /REST/Service/Editable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    private Integer visitObjectId;
    private Integer categoryVisitorId;
    private Integer cost;
}
