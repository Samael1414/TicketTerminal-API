package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * DTO для создания новой категории посетителя.
 *
 * Используется только при создании записи, не содержит ID.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CategoryVisitorCreateDto {

    @JsonProperty("CategoryVisitorName")
    private String categoryVisitorName;
}
