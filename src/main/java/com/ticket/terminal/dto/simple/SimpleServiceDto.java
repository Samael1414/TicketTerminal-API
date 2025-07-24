package com.ticket.terminal.dto.simple;

/*
# Получение списка простых услуг, доступных для продажи
---
## GET /REST/Service/Simple
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ticket.terminal.dto.SeanceGridDto;
import lombok.*;
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
    private Double cost;

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

    @JsonProperty("dtBegin")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Moscow")
    private String dtBegin;

    @JsonProperty("dtEnd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Moscow")
    private String dtEnd;

    @JsonProperty("IsPROCultureChecked")
    private Boolean isProCultureChecked;

    @JsonProperty("SeanceGrid")
    private List<SeanceGridDto> seanceGrid;

    @JsonProperty("ActiveDays")
    private Integer activeDays;


}
