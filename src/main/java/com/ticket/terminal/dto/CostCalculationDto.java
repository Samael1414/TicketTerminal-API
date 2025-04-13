package com.ticket.terminal.dto;
/*
POST /REST/Order/Cost
 */
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostCalculationDto {

    private Long serviceId;
    private List<Long> visitObjectId;
    private List<CategoryVisitorCountDto> categoryVisitor;

}
