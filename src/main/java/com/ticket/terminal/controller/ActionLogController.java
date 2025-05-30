package com.ticket.terminal.controller;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.service.ActionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Logs")
@RequiredArgsConstructor
public class ActionLogController {

    private final ActionLogService actionLogService;

    @Operation(summary = "Получение всех логов действий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<ActionLogDto>> findAll() {
        List<ActionLogDto> logs = actionLogService.findAll();
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Удаление логов старше 120 дней")
    @DeleteMapping("/Delete")
    public ResponseEntity<Void> delete() {
        actionLogService.deleteLogsRange();
        return ResponseEntity.ok().build();
    }

}
