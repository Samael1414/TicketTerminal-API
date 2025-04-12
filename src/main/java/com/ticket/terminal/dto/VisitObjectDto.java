package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
DTO для объектов посещения (VisitObjectDto)
GET /REST/Service/Editable
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitObjectDto {

    private Long visitObjectId;
    private String visitObjectName;
    private boolean required;
}
