package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VersionDto {

    private List<GateInfoDto> gate;
    private List<SystemInfoDto> system;
    private List<RequisiteInfoDto> requisite;
}
