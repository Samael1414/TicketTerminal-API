package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisiteInfoDto {

    private String name;
    private String city;
    private String address;
    private String phone1;
    private String fax;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
}
