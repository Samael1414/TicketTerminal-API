/**
 * Контроллер для управления экскурсиями (Excursion).
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с получением и созданием экскурсий.
 * - Взаимодействует с ExcursionService для выполнения бизнес-логики.
 * 
 * Принимает и возвращает DTO:
 * - ExcursionDto, ExcursionRequestDto, ExcursionResponseDto, ExcursionListResponseDto и др.
 * 
 * Основные методы:
 * - getAllExcursions: Получить список всех экскурсий
 * - getExcursionById: Получить экскурсию по ID
 * - createExcursion: Создать новую экскурсию
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.ExcursionListResponseDto;
import com.ticket.terminal.dto.ExcursionRequestDto;
import com.ticket.terminal.dto.ExcursionResponseDto;
import com.ticket.terminal.service.ExcursionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Excursion")
public class ExcursionController {

    private final ExcursionService excursionService;

    @Operation(summary = "Получение всех экскурсий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/List")
    public ExcursionListResponseDto getAllExcursion() {
        return excursionService.getAllExcursions();
    }

    @Operation(summary = "Создание экскурсии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Booking")
    public ResponseEntity<ExcursionResponseDto> createExcursion(@RequestBody ExcursionRequestDto dto) {
        return ResponseEntity.ok(excursionService.createExcursion(dto));
    }
}
