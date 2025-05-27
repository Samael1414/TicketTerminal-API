package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalTime;

@Data
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class RequisiteInfoDto {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Phone1")
    private String phone1;

    @JsonProperty("Fax")
    private String fax;

    private LocalTime dtBegin;

    private LocalTime dtEnd;
}
