package com.ticket.terminal.dto;

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
public class GateInfoDto {

    private String name;
    private String version;
    private Integer major;
    private Integer minor;
    private Integer release;
    private Integer build;
    private OffsetDateTime dtLicenceFinish;
}
