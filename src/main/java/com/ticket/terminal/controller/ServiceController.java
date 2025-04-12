package com.ticket.terminal.controller;


import com.ticket.terminal.dto.EditableServiceDto;
import com.ticket.terminal.dto.SimpleServiceResponseDto;
import com.ticket.terminal.service.ServiceService;
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
@RequestMapping("/TLMuseumGate/REST")
public class ServiceController {


    private final ServiceService serviceService;


    @Operation(summary = "Получить список простых услуг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Service/Simple")
    public ResponseEntity<SimpleServiceResponseDto> getSimpleService() {
        return ResponseEntity.ok(serviceService.getSimpleService());
    }

    @Operation(summary = "Получить список редактируемых услуг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Service/Editable")
    public ResponseEntity<List<EditableServiceDto>> getEditableServices() {
        List<EditableServiceDto> services = serviceService.getEditableServices();
        return ResponseEntity.ok(services);
    }
}
