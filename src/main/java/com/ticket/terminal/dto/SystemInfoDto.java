package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SystemInfoDto {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Major")
    private Integer major;

    @JsonProperty("Minor")
    private Integer minor;

    @JsonProperty("Release")
    private Integer release;

    @JsonProperty("Build")
    private Integer build;

    private OffsetDateTime dtLicenceFinish;
}
