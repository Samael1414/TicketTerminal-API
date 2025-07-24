/**
 * Контроллер для управления услугами (Service).
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с получением, созданием и обновлением услуг.
 * - Взаимодействует с ServiceService для выполнения бизнес-логики.
 * 
 * Принимает и возвращает DTO:
 * - ServiceDto, EditableServiceDto, EditableServiceResponseDto, SimpleServiceDto, SimpleServiceResponseDto и др.
 * 
 * Основные методы:
 * - getAllServices: Получить список всех услуг
 * - getServiceById: Получить услугу по ID
 * - createService: Создать новую услугу
 * - updateService: Обновить данные услуги
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.editable.EditableServiceDto;
import com.ticket.terminal.dto.editable.EditableServiceResponseDto;
import com.ticket.terminal.dto.service.ServiceCreateDto;
import com.ticket.terminal.dto.service.ServiceUpdateDto;
import com.ticket.terminal.dto.simple.SimpleServiceResponseDto;
import com.ticket.terminal.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Service")
public class ServiceController {


    private final ServiceService serviceService;


    @Operation(summary = "Получить список простых услуг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Простые услуги успешно найдены"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Simple")
    public ResponseEntity<SimpleServiceResponseDto> getSimpleService() {
        return ResponseEntity.ok(serviceService.getSimpleService());
    }

    @Operation(summary = "Получить список редактируемых услуг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Услуги для терминала успешно найдены"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Editable")
    public ResponseEntity<EditableServiceResponseDto> getEditableServices() {
        return ResponseEntity.ok(serviceService.getEditableServices());
    }

    @Operation(summary = "Получить полную информацию об услуге по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Услуга успешно найдена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EditableServiceDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.findById(id));
    }

    @Operation(summary = "Создать новую услугу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Услуга успешно создана"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })

    @PostMapping("/Create")
    public ResponseEntity<EditableServiceDto> createService(@Valid @RequestBody ServiceCreateDto dto) {
        EditableServiceDto response = serviceService.createService(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Обновить услугу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Услуга успешно обновлена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })

    @PutMapping("/Update/{id}")
    public ResponseEntity<EditableServiceDto> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceUpdateDto dto) {
        EditableServiceDto updatedService = serviceService.updateService(id, dto);
        return ResponseEntity.ok(updatedService);
    }

    @Operation(summary = "Удалить услугу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Услуга успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }



}
