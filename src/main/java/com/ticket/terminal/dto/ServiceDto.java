package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {

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
