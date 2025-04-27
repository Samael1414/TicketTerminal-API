package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class VersionDto {

    @JsonProperty("Gate")
    private List<GateInfoDto> gate;

    @JsonProperty("System")
    private List<SystemInfoDto> system;

    @JsonProperty("Requisite")
    private List<RequisiteInfoDto> requisite;
}
