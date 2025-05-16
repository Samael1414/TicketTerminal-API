package com.ticket.terminal.controller;

import com.ticket.terminal.dto.VisitObjectCreateDto;
import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.service.VisitObjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления объектами посещения (VisitObject).
 *
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с CRUD-операциями над объектами посещения.
 * - Делегирует бизнес-логику в слой Service.
 *
 * URL prefix: /REST/visit-objects
 */
@RestController
@RequestMapping("/VisitObject")
@RequiredArgsConstructor
@Tag(name = "Visit Objects", description = "Управление объектами посещения")
public class VisitObjectController {

    private final VisitObjectService visitObjectService;

    /**
     * Получить список всех объектов посещения.
     *
     * @return список VisitObjectDto
     */
    @Operation(summary = "Получить все объекты посещения")
    @GetMapping
    public ResponseEntity<List<VisitObjectDto>> getAllVisitObjects() {
        return ResponseEntity.ok(visitObjectService.getAllVisitObjects());
    }

    /**
     * Создать новый объект посещения.
     *
     * @param dto входной DTO с полями для создания
     * @return созданный VisitObjectDto
     */
    @Operation(summary = "Создать объект посещения")
    @PostMapping("/Create")
    public ResponseEntity<VisitObjectDto> create(@Valid @RequestBody VisitObjectCreateDto dto) {
        return ResponseEntity.ok(visitObjectService.createVisitObject(dto));
    }

    /**
     * Обновить объект посещения по ID.
     *
     * @param id идентификатор редактируемого объекта
     * @param dto обновлённые поля объекта
     * @return обновлённый VisitObjectDto
     */
    @Operation(summary = "Обновить объект посещения по ID")
    @PutMapping("/Update/{id}")
    public ResponseEntity<VisitObjectDto> update(@PathVariable Long id,
                                                 @Valid @RequestBody VisitObjectCreateDto dto) {
        return ResponseEntity.ok(visitObjectService.updateVisitObject(id, dto));
    }

    /**
     * Удалить объект посещения по ID.
     *
     * @param id идентификатор объекта
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Удалить объект посещения по ID")
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitObjectService.deleteVisitObject(id);
        return ResponseEntity.noContent().build();
    }
}
