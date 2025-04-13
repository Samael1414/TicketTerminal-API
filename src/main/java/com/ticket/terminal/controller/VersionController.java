package com.ticket.terminal.controller;

import com.ticket.terminal.dto.VersionDto;
import com.ticket.terminal.service.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TLMuseumGate/REST")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @Operation(summary = "Получить текущую версию API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Версия не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Version")
    public ResponseEntity<VersionDto> getVersion() {
        VersionDto versionDto = versionService.getAllVersion();
        return ResponseEntity.ok(versionDto);
    }
}


