/**
 * DTO для передачи информации о действиях пользователя (Action Log).
 * 
 * Назначение:
 * - Используется для логирования и отображения действий пользователей в системе.
 * - Применяется в контроллерах, сервисах и для аудита.
 * 
 * Содержит данные:
 * - userId (ID пользователя)
 * - actionType (тип действия)
 * - description (описание действия)
 * - createdAt (дата и время действия)
 * - actorName (имя исполнителя)
 */
package com.ticket.terminal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ActionLogDto {

    private Long userId;
    private String actionType;
    private String description;
    private LocalDateTime createdAt;
    private String actorName;
}
