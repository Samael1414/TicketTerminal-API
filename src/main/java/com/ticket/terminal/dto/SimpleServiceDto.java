package com.ticket.terminal.dto;

/*
# Получение списка простых услуг, доступных для продажи
---
## GET /REST/Service/Simple
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SimpleServiceDto {

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Comment")
    private String comment;

    @JsonProperty("Cost")
    private Integer cost;

    @JsonProperty("ActiveKindId")
    private Integer activeKindId;

    @JsonProperty("IsNeedVisitDate")
    private Boolean isNeedVisitDate;

    @JsonProperty("ProCultureIdentifier")
    private Integer proCultureIdentifier;

    @JsonProperty("IsNeedVisitTime")
    private Boolean isNeedVisitTime;

    @JsonProperty("PaymentKindId")
    private List<Integer> paymentKindId;

    @JsonProperty("Dates")
    private List<OffsetDateTime> dates;

    private LocalTime dtBegin;

    private LocalTime dtEnd;

    @JsonProperty("IsPROCultureChecked")
    private Boolean isProCultureChecked;

    @JsonProperty("SeanceGrid")
    private List<SeanceGridDto> seanceGrid;

    @JsonProperty("ActiveDays")
    private Integer activeDays;


}
