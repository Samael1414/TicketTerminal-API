package com.ticket.terminal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class VisitObjectCreateDto {

    @NotBlank
    private String visitObjectName;
    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;
    private String address;
    private String comment;
    private Boolean isRequire;
    @JsonProperty("GroupVisitObjectId")
    private Long groupVisitObjectId;
}