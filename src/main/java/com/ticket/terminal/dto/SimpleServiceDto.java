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

    private String serviceName;
    // comment нет в ответах json, но есть ниже в инструкции нужно ли добавить @JsonInclude?
    private String comment;
    private Integer cost;
    private Integer activeKindId;
    private Boolean isNeedVisitDate;
    private Integer proCultureIdentifier;
    private Boolean isNeedVisitTime;
    private List<Integer> paymentKindIds;
    private List<OffsetDateTime> dates;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private Boolean isProCultureChecked;
    private List<SeanceGridDto> seanceGrid;
    private Integer activeDays;


}
