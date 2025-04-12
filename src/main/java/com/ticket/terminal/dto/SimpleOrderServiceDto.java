package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleOrderServiceDto {

    private Long serviceId;
    private Integer serviceCost;
    private Integer serviceCount;
    private OffsetDateTime dtVisit;
}
