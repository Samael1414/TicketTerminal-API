package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalTime;
/*
Сетка времени для SimpleServiceDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceGridDto {

    private LocalTime dtBegin;
    private LocalTime dtEnd;
}
