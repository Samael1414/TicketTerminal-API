package com.ticket.terminal.dto;
/*
POST /REST/Order/Cost
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CostCalculationDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("VisitObjectId")
    private List<Long> visitObjectId;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorCountDto> categoryVisitor;

}
