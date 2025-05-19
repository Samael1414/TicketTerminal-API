package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class VisitObjectDto {

    @JsonProperty("VisitObjectId")
    private Long visitObjectId;

    @JsonProperty("VisitObjectName")
    private String visitObjectName;

    @JsonProperty("IsRequire")
    private Boolean isRequire;

    @JsonProperty("GroupVisitObjectId")
    private Long groupVisitObjectId;

    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Comment")
    private String comment;
}
