package com.ticket.terminal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.OffsetDateTime;
/*
Получение информации о версии шлюза
---
## GET /REST/Version
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class GateInfoDto {

    private String name;
    private String version;
    private Integer major;
    private Integer minor;
    private Integer release;
    private Integer build;
    private OffsetDateTime dtLicenceFinish;
}
