/**
 * Контроллер для управления объектами посещения (VisitObject).
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с получением информации об объектах посещения.
 * - Взаимодействует с VisitObjectService для выполнения бизнес-логики.
 * 
 * Принимает и возвращает DTO:
 * - VisitObjectDto, VisitObjectItemDto и др.
 * 
 * Основные методы:
 * - getAllVisitObjects: Получить список всех объектов посещения
 * - getVisitObjectById: Получить объект посещения по ID
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.service.VisitObjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/REST/Service")
@RequiredArgsConstructor
public class VisitObjectController {

    private final VisitObjectService visitObjectService;

    @Operation(summary = "Получение объектов посещения")
    @GetMapping("/Editable")
    public ResponseEntity<List<VisitObjectDto>> getAllVisitObjects() {
        return ResponseEntity.ok(visitObjectService.getAllVisitObjects());
    }
}
