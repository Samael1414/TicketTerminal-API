package com.ticket.terminal.controller;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Service")
public class ServiceController {


    private final ServiceService serviceService;


    @Operation(summary = "Получить список простых услуг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
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
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Editable")
    public ResponseEntity<EditableServiceResponseDto> getEditableServices() {
        return ResponseEntity.ok(serviceService.getEditableServices());
    }

//    @GetMapping("/Full")
//    public ResponseEntity<TLMuseumServiceResponseDto> getFullMuseumServiceResponse() {
//        return ResponseEntity.ok(serviceService.getFullMuseumServiceResponse());
//    }
}
