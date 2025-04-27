package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ServiceDto {

    @JsonProperty("ServiceId")
    private Integer serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Cost")
    private Integer cost;

    @JsonProperty("ActiveKindId")
    private Integer activeKindId;

    @JsonProperty("IsNeedVisitDate")
    private Boolean isNeedVisitDate;

    @JsonProperty("IsNeedVisitTime")
    private Boolean isNeedVisitTime;

    private LocalTime dtBegin;

    private LocalTime dtEnd;

    @JsonProperty("ProCultureIdentifier")
    private Integer proCultureIdentifier;

    @JsonProperty("IsPROCultureChecked")
    private Boolean isPROCultureChecked;
}
