package com.ticket.terminal.dto;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PROCultureGUIDDto {

    private List<PROCultureGUIDEntryDto> seat;

}
