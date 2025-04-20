package com.ticket.terminal.dto;

/*
# Получение списка простых услуг, доступных для продажи
---
## GET /REST/Service/Simple
 */
import lombok.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleServiceDto {

    private Long serviceId;
    private String serviceName;
    private String comment;
    private Integer cost;
    private Integer activeKindId;
    private Boolean needVisitDate;
    private Integer proCultureIdentifier;
    private Boolean needVisitTime;
    private List<Integer> paymentKindIds;
    private List<OffsetDateTime> dates;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private Boolean proCultureChecked;
    private List<SeanceGridDto> seanceGrid;
    private Integer activeDays;


}
