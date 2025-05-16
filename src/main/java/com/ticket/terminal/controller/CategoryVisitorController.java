package com.ticket.terminal.controller;

import com.ticket.terminal.dto.CategoryVisitorCreateDto;
import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.service.CategoryVisitorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/REST/CategoryVisitors")
@RequiredArgsConstructor
public class CategoryVisitorController {

    private final CategoryVisitorService categoryVisitorService;

    /**
     * Получить список всех категорий посетителей
     */
    @Operation(summary = "Получение всех категорий посетителей")
    @GetMapping
    public ResponseEntity<List<CategoryVisitorDto>> getAll() {
        return ResponseEntity.ok(categoryVisitorService.getAll());
    }

    /**
     * Создать новую категорию
     */
    @Operation(summary = "Создание категории посетителя")
    @PostMapping("/Create")
    public ResponseEntity<CategoryVisitorDto> create(@Valid @RequestBody CategoryVisitorCreateDto dto) {
        return ResponseEntity.ok(categoryVisitorService.create(dto));
    }

    /**
     * Обновить существующую категорию.
     *
     * @param id  идентификатор категории
     * @param dto данные для обновления
     * @return обновлённая категория
     */
    @Operation(summary = "Обновить категорию посетителя")
    @PutMapping("/Update/{id}")
    public ResponseEntity<CategoryVisitorDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody CategoryVisitorCreateDto dto) {
        return ResponseEntity.ok(categoryVisitorService.update(id, dto));
    }

    /**
     * Удалить категорию по ID
     */
    @Operation(summary = "Удаление категории по ID")
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryVisitorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
