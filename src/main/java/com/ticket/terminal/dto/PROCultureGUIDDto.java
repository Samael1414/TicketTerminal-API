package com.ticket.terminal.dto;

/*
DTO для передачи GUID от PRO Культуры
Эндпоинт:

POST /REST/SetPROCultureGUID
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PROCultureGUIDDto {

    private List<PROCultureGUIDEntryDto> seat;

}
