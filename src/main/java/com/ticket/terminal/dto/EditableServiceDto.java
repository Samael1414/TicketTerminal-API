package com.ticket.terminal.dto;

/*
# Получение списка редактируемых услуг, доступных для продажи
---
## GET /REST/Service/Editable

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
public class EditableServiceDto {

    private Long serviceId; // Уникальный идентификатор услуги
    private String serviceName;  // Наименование услуги
    //comment нет в ответах json, но есть ниже в инструкции нужно ли добавить @JsonInclude?
    private String comment;  // Описание услуги (может отсутствовать)
    private boolean isDisableEditVisitObject; // Запрещено изменять список объектов посещения
    private boolean isDisableEditVisitor; // Запрещено изменять количество и категории посетителей
    private boolean isVisitObjectUseForCost;  // Объекты посещения используются для расчета стоимости
    private boolean isCategoryVisitorUseForCost; // Категории посетителей используются для расчета стоимости
    private boolean isVisitorCountUseForCost; // Количество посетителей используется для расчета стоимости
    private boolean isUseOneCategory; // Можно выбрать только одну категорию посетителей
    private Integer maxVisitObjectCount;
    private Integer maxVisitorCount;
    private Integer activeKindId;
    private boolean isNeedVisitDate;
    private boolean isNeedVisitTime;
    private Integer activeDays;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private List<OffsetDateTime> dates;
    private Integer proCultureIdentifier;
    private boolean isProCultureChecked;
    private List<Integer> paymentKindIds;
    private List<VisitObjectDto> visitObjects;
    private List<CategoryVisitorDto> categoryVisitors;
    private List<PriceDto> prices;

}
