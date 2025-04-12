package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
DTO для категорий посетителей (CategoryVisitorDto)
GET /REST/Service/Editable
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVisitorDto {

    private Long categoryVisitorId;
    private String categoryVisitorName;
    private Integer requireVisitorCount;
}
