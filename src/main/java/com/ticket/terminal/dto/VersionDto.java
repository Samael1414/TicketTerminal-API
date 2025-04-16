package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionDto {

    private List<GateInfoDto> gate;
    private List<SystemInfoDto> system;
    private List<RequisiteInfoDto> requisite;
}
