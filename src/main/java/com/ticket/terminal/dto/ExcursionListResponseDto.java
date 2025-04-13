package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionListResponseDto {

    private List<ExcursionDto> service;
    private List<VisitObjectDto> visitObject;
    private List<CategoryVisitorDto> categoryVisitor;
}
