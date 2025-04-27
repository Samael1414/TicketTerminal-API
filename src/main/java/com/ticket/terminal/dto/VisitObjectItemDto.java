package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class VisitObjectItemDto {

    @JsonProperty("VisitObjectId")
    private Long visitObjectId;

    @JsonProperty("VisitObjectName")
    private String visitObjectName;

    @JsonProperty("IsRequire")
    private boolean isRequire;

    @JsonProperty("GroupVisitObjectId")
    private Long groupVisitObjectId;
}
