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
@RequestMapping("/TLMuseumGate/REST")
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
    @GetMapping("/Excursion")
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
    @PostMapping("/Excursion/Booking")
    public ResponseEntity<ExcursionResponseDto> createExcursion(@RequestBody ExcursionRequestDto dto) {
        ExcursionResponseDto response = excursionService.createExcursion(dto);
        return ResponseEntity.ok(response);
    }
}
