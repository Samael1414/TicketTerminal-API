package com.ticket.terminal.dto.group;

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
public class GroupVisitObjectDto {

    @JsonProperty("GroupVisitObjectId")
    private Long groupVisitObjectId;

    @JsonProperty("GroupVisitObjectName")
    private String groupVisitObjectName;
}
