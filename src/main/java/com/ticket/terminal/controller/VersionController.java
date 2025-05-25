/**
 * Контроллер для получения информации о версии приложения.
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы для получения информации о версии системы.
 * - Взаимодействует с VersionService для получения информации.
 * 
 * Принимает и возвращает DTO:
 * - VersionDto, SystemInfoDto и др.
 * 
 * Основные методы:
 * - getVersion: Получить текущую версию приложения
 * - getSystemInfo: Получить информацию о системе
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.VersionDto;
import com.ticket.terminal.service.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Version")
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
    @GetMapping("/Info")
    public ResponseEntity<VersionDto> getVersion() {
        return ResponseEntity.ok(versionService.getAllVersion());
    }

    @Operation(summary = "Обновить данные версии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Update")
    public ResponseEntity<Void> updateVersion(@RequestBody VersionDto versionDto) {
        versionService.updateAllVersion(versionDto);
        return ResponseEntity.ok().build();
    }
}


