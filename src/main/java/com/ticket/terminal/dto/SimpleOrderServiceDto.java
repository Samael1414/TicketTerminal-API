package com.ticket.terminal.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleOrderServiceDto {

    private Long serviceId;
    private Integer serviceCost;
    private Integer serviceCount;
    private OffsetDateTime dtVisit;
}
