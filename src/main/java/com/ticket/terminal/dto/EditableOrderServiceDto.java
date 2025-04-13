package com.ticket.terminal.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditableOrderServiceDto {

    private Long serviceId;
    private Integer serviceCost;
    private Integer serviceCount;
    private OffsetDateTime dtVisit;
    private List<Long> visitObjectId;
    private List<CategoryVisitorCountDto> categoryVisitor;
}
