/**
 * DTO для передачи информации о категории посетителя (Category Visitor).
 * 
 * Назначение:
 * - Используется для передачи сведений о типе посетителя (например, взрослый, ребёнок, льготник) между слоями приложения и внешними системами.
 * - Применяется в контроллерах, сервисах, клиентах и для обмена с внешними API.
 * 
 * Содержит данные:
 * - id категории, наименование, описание, параметры и др.
 */
package com.ticket.terminal.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
/*
DTO для категорий посетителей (CategoryVisitorDto)
GET /REST/Service/Editable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CategoryVisitorDto {

    @JsonProperty("CategoryVisitorId")
    private Long categoryVisitorId;

    @JsonProperty("CategoryVisitorName")
    private String categoryVisitorName;

    @JsonProperty("RequireVisitorCount")
    private Integer requireVisitorCount;

    @JsonProperty("GroupCategoryVisitorId")
    private Long groupCategoryVisitorId;
}
