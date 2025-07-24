package com.ticket.terminal.dto.culture;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class PROCultureGUIDDto {

    @JsonProperty("Seat")
    private List<PROCultureGUIDEntryDto> seat;

}
