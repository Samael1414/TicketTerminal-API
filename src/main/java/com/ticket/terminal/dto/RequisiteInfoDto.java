package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisiteInfoDto {

    private String name;
    private String city;
    private String address;
    private String phone1;
    private String fax;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
}
