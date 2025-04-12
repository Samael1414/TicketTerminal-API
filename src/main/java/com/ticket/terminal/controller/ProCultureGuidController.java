package com.ticket.terminal.controller;

import com.ticket.terminal.dto.PROCultureGUIDDto;
import com.ticket.terminal.service.ProCultureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TLMuseumGate/REST")
@RequiredArgsConstructor
public class ProCultureGuidController {

    private final ProCultureService proCultureService;

    @Operation(summary = "Сохранение ProCulture GUID по заказу или месту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GUID успешно сохранён"),
            @ApiResponse(responseCode = "400", description = "Ошибка в теле запроса или недопустимые данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Связанный заказ или место не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/SetPROCultureGUID")
    public ResponseEntity<Void> setPROCultureGUID (@RequestBody PROCultureGUIDDto dto) {
        proCultureService.saveProCultureGUIDs(dto);
        return ResponseEntity.ok().build();
    }
}
