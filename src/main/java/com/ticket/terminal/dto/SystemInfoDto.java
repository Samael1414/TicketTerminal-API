package com.ticket.terminal.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoDto {

    private String name;
    private String version;
    private Integer major;
    private Integer minor;
    private Integer release;
    private Integer build;
    private OffsetDateTime dtLicenceFinish;
}
