package com.ticket.terminal.controller;

import com.ticket.terminal.dto.UserPermissionDto;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.mapper.UserPermissionMapper;
import com.ticket.terminal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с правами доступа
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/Permissions")
public class PermissionController {

    private final UserService userService;
    private final UserPermissionMapper permissionMapper;

    /**
     * Получение прав доступа текущего пользователя
     */
    @Operation(summary = "Получить права доступа текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Current")
    public ResponseEntity<UserPermissionDto> getCurrentUserPermissions() {
        UsersEntity user = userService.getCurrentUser();
        if (user.getPermissions() == null) {
            return ResponseEntity.ok(new UserPermissionDto());
        }
        return ResponseEntity.ok(permissionMapper.toDto(user.getPermissions()));
    }

    /**
     * Получение всех прав доступа текущего пользователя в виде списка строк
     */
    @Operation(summary = "Получить список прав доступа текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/List")
    public ResponseEntity<Map<String, Boolean>> getCurrentUserPermissionsList(Authentication authentication) {
        Map<String, Boolean> permissions = new HashMap<>();
        
        // Получаем все авторитеты пользователя
        if (authentication != null && authentication.isAuthenticated()) {
            permissions = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("CAN_") || auth.equals("ROOT"))
                    .collect(Collectors.toMap(
                            auth -> auth,
                            auth -> true
                    ));
        }
        
        // Добавляем все возможные права доступа со значением false, если они не были добавлены ранее
        permissions.putIfAbsent("CAN_MANAGE_USERS", false);
        permissions.putIfAbsent("CAN_MANAGE_SERVICES", false);
        permissions.putIfAbsent("CAN_MANAGE_CATEGORIES", false);
        permissions.putIfAbsent("CAN_MANAGE_VISIT_OBJECTS", false);
        permissions.putIfAbsent("CAN_VIEW_REPORTS", false);
        permissions.putIfAbsent("CAN_MANAGE_SETTINGS", false);
        permissions.putIfAbsent("CAN_MANAGE_ORDERS", false);
        permissions.putIfAbsent("CAN_EXPORT_DATA", false);
        permissions.putIfAbsent("CAN_IMPORT_DATA", false);
        permissions.putIfAbsent("ROOT", false);
        
        return ResponseEntity.ok(permissions);
    }
}
