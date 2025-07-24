package com.ticket.terminal.dto.cost;
/*
DTO для расчета стоимости (ответ сервера)
Эндпоинт: POST /REST/Order/Cost
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CostResponseDto {

    @JsonProperty("Cost")
    private Double cost;
}
