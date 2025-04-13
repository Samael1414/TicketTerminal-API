package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleServiceResponseDto {

    private List<SimpleServiceDto> services;
    private List<SeanceGridDto> seanceGrid;
}
