package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

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
    private Double cost;

    @JsonProperty("ActiveKindId")
    private Integer activeKindId;

    @JsonProperty("IsNeedVisitDate")
    private Boolean isNeedVisitDate;

    @JsonProperty("IsNeedVisitTime")
    private Boolean isNeedVisitTime;

    @JsonProperty("dtBegin")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dtBegin;

    @JsonProperty("dtEnd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dtEnd;

    @JsonProperty("ProCultureIdentifier")
    private Integer proCultureIdentifier;

    @JsonProperty("IsPROCultureChecked")
    private Boolean isPROCultureChecked;
}
