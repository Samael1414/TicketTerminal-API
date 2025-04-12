package com.ticket.terminal.dto;

/*
POST /REST/Order/Cost
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostCalculationDto {

    private Long serviceId;
    private List<Long> visitObjectId;
    private List<CategoryVisitorCountDto> categoryVisitor;

}
