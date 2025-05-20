/**
 * Контроллер для управления организациями (Organization).
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с получением и созданием организаций.
 * - Взаимодействует с OrganizationService для выполнения бизнес-логики.
 * 
 * Принимает и возвращает DTO:
 * - OrganizationDto и др.
 * 
 * Основные методы:
 * - getAllOrganizations: Получить список всех организаций
 * - getOrganizationById: Получить организацию по ID
 * - createOrganization: Создать новую организацию
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.OrganizationDto;
import com.ticket.terminal.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Info")
public class OrganizationController {

    private final OrganizationService organizationService;


    @Operation(summary = "Получить список организаций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Org")
    public ResponseEntity<List<OrganizationDto>> getOrganization() {
        List<OrganizationDto> organizationDtoList = organizationService.getAllOrganization();
        return ResponseEntity.ok(organizationDtoList);
    }

}
