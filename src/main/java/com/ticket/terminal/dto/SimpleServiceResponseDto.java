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
public class SimpleServiceResponseDto {

    private List<SimpleServiceDto> services;
    private List<SeanceGridDto> seanceGrid;
}
