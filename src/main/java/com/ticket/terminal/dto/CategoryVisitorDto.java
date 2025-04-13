package com.ticket.terminal.dto;

import lombok.*;
/*
DTO для категорий посетителей (CategoryVisitorDto)
GET /REST/Service/Editable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVisitorDto {

    private Long categoryVisitorId;
    private String categoryVisitorName;
    private Integer requireVisitorCount;
}
