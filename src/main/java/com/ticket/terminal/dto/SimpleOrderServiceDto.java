package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SimpleOrderServiceDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("OrderServiceId")
    private Long orderServiceId;

    @JsonProperty("ServiceCost")
    private Double serviceCost;

    @JsonProperty("ServiceCount")
    private Integer serviceCount;

    @JsonAlias("dtVisit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Moscow")
    private ZonedDateTime dtVisit;

    @JsonAlias("dtDrop")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dtDrop;

    @JsonProperty("VisitObjectId")
    private List<Long> visitObjectId;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorCountDto> categoryVisitor;
}
