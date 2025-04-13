package com.ticket.terminal.dto;

import lombok.*;

/*
DTO для объектов посещения (VisitObjectDto)
GET /REST/Service/Editable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitObjectDto {

    private Long visitObjectId;
    private String visitObjectName;
    private Boolean required;
}
