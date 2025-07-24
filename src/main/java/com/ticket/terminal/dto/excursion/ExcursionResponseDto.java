package com.ticket.terminal.dto.excursion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ExcursionResponseDto {

    @JsonProperty("ExcursionLogId")
    private Long excursionLogId;

    @JsonProperty("ExcursionLogInternetNumber")
    private Long excursionLogInternetNumber;
}
