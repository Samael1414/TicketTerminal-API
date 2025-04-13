package com.ticket.terminal.dto;
/*
# Получение списка редактируемых услуг, доступных для продажи
---
## GET /REST/Service/Editable

 */
import lombok.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditableServiceDto {

    private Long serviceId; // Уникальный идентификатор услуги
    private String serviceName;  // Наименование услуги
    //comment нет в ответах json, но есть ниже в инструкции нужно ли добавить @JsonInclude?
    private String comment;  // Описание услуги (может отсутствовать)
    private Boolean isDisableEditVisitObject; // Запрещено изменять список объектов посещения
    private Boolean isDisableEditVisitor; // Запрещено изменять количество и категории посетителей
    private Boolean isVisitObjectUseForCost;  // Объекты посещения используются для расчета стоимости
    private Boolean isCategoryVisitorUseForCost; // Категории посетителей используются для расчета стоимости
    private Boolean isVisitorCountUseForCost; // Количество посетителей используется для расчета стоимости
    private Boolean isUseOneCategory; // Можно выбрать только одну категорию посетителей
    private Integer maxVisitObjectCount;
    private Integer maxVisitorCount;
    private Integer activeKindId;
    private Boolean isNeedVisitDate;
    private Boolean isNeedVisitTime;
    private Integer activeDays;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private List<OffsetDateTime> dates;
    private Integer proCultureIdentifier;
    private Boolean isProCultureChecked;
    private List<Integer> paymentKindIds;
    private List<VisitObjectDto> visitObjects;
    private List<CategoryVisitorDto> categoryVisitors;
    private List<PriceDto> prices;

}
