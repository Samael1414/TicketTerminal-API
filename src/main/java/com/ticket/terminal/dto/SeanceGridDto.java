package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalTime;
import java.util.List;

/*
Сетка времени для SimpleServiceDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SeanceGridDto {

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("dtBegin")
    private String dtBegin;

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("dtEnd")
    private String dtEnd;
}
