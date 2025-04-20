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
    private Boolean disableEditVisitObject; // Запрещено изменять список объектов посещения
    private Boolean disableEditVisitor; // Запрещено изменять количество и категории посетителей
    private Boolean visitObjectUseForCost;  // Объекты посещения используются для расчета стоимости
    private Boolean categoryVisitorUseForCost; // Категории посетителей используются для расчета стоимости
    private Boolean visitorCountUseForCost; // Количество посетителей используется для расчета стоимости
    private Boolean useOneCategory; // Можно выбрать только одну категорию посетителей
    private Integer maxVisitObjectCount;
    private Integer maxVisitorCount;
    private Integer activeKindId;
    private Boolean needVisitDate;
    private Boolean needVisitTime;
    private Integer activeDays;
    private LocalTime dtBegin;
    private LocalTime dtEnd;
    private List<OffsetDateTime> dates;
    private Integer proCultureIdentifier;
    private Boolean proCultureChecked;
    private List<Integer> paymentKindIds;
    private List<VisitObjectDto> visitObjects;
    private List<CategoryVisitorDto> categoryVisitors;
    private List<PriceDto> prices;

}
