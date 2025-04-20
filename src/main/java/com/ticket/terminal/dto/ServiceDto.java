package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {

    private Integer serviceId;
    private String serviceName;
    private String description;
    private Integer cost;
    private Integer activeKindId;
    private Boolean needVisitDate;
    private Boolean needVisitTime;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private Integer proCultureIdentifier;
    private Boolean proCultureChecked;
}
