package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
/*
Сетка времени для SimpleServiceDto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeanceGridDto {

    private LocalTime dtBegin;
    private LocalTime dtEnd;
}
