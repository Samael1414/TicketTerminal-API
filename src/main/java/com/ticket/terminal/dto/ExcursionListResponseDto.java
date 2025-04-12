package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionListResponseDto {

    private List<ExcursionDto> service;
    private List<VisitObjectDto> visitObject;
    private List<CategoryVisitorDto> categoryVisitor;
}
