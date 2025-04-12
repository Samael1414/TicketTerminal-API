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
public class SystemInfoDto {

    private String name;
    private String version;
    private Integer major;
    private Integer minor;
    private Integer release;
    private Integer build;
    private OffsetDateTime dtLicenceFinish;
}
