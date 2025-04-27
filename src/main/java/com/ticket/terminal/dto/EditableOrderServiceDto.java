package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EditableOrderServiceDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("ServiceCost")
    private Integer serviceCost;

    @JsonProperty("ServiceCount")
    private Integer serviceCount;

    @JsonProperty("dtVisit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dtVisit;

    @JsonProperty("VisitObjectId")
    private List<Long> visitObjectId;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorCountDto> categoryVisitor;
}
