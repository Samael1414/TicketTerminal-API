package com.ticket.terminal.dto;

/*
# Получение списка простых услуг, доступных для продажи
---
## GET /REST/Service/Simple
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleServiceDto {

    private String serviceName;
    // comment нет в ответах json, но есть ниже в инструкции нужно ли добавить @JsonInclude?
    private String comment;
    private Integer cost;
    private Integer activeKindId;
    private boolean isNeedVisitDate;
    private Integer proCultureIdentifier;
    private boolean isNeedVisitTime;
    private List<Integer> paymentKindIds;
    private List<OffsetDateTime> dates;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private boolean isProCultureChecked;
    private List<SeanceGridDto> seanceGrid;
    private Integer activeDays;


}
