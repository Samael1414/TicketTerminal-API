package com.ticket.terminal.controller;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить список пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<UsersResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Найти пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<UsersResponseDto> createUser(@RequestBody UsersCreateDto dto) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsersResponseDto> update(@PathVariable Long id, @RequestBody UsersCreateDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Удалить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
