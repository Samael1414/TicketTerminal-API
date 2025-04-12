package com.ticket.terminal.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {

    private String name;
    private String address;
    private String city;
    private String phone1;
    private String phone2;
    private String fax;
    private String dtBegin;
    private String dtEnd;
}
