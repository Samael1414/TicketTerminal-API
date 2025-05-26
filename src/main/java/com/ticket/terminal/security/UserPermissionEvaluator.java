package com.ticket.terminal.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Компонент для проверки прав доступа пользователя
 */
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

    /**
     * Проверяет, имеет ли пользователь право на управление пользователями
     */
    public boolean canManageUsers(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_USERS");
    }

    /**
     * Проверяет, имеет ли пользователь право на управление услугами
     */
    public boolean canManageServices(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_SERVICES");
    }

    /**
     * Проверяет, имеет ли пользователь право на управление категориями
     */
    public boolean canManageCategories(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_CATEGORIES");
    }

    /**
     * Проверяет, имеет ли пользователь право на управление объектами посещения
     */
    public boolean canManageVisitObjects(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_VISIT_OBJECTS");
    }

    /**
     * Проверяет, имеет ли пользователь право на просмотр отчетов
     */
    public boolean canViewReports(Authentication authentication) {
        return hasAuthority(authentication, "CAN_VIEW_REPORTS");
    }

    /**
     * Проверяет, имеет ли пользователь право на управление настройками
     */
    public boolean canManageSettings(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_SETTINGS");
    }

    /**
     * Проверяет, имеет ли пользователь право на управление заказами
     */
    public boolean canManageOrders(Authentication authentication) {
        return hasAuthority(authentication, "CAN_MANAGE_ORDERS");
    }

    /**
     * Проверяет, имеет ли пользователь право на экспорт данных
     */
    public boolean canExportData(Authentication authentication) {
        return hasAuthority(authentication, "CAN_EXPORT_DATA");
    }

    /**
     * Проверяет, имеет ли пользователь право на импорт данных
     */
    public boolean canImportData(Authentication authentication) {
        return hasAuthority(authentication, "CAN_IMPORT_DATA");
    }

    /**
     * Проверяет, является ли пользователь root-пользователем
     */
    public boolean isRoot(Authentication authentication) {
        return hasAuthority(authentication, "ROOT");
    }

    /**
     * Проверяет наличие указанного права доступа у пользователя
     */
    private boolean hasAuthority(Authentication authentication, String authority) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Проверяем наличие указанного права доступа
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(authority) || auth.equals("ROOT"));
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }
        
        String permissionString = (String) permission;
        
        // Проверяем наличие права доступа
        return hasAuthority(authentication, permissionString);
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }
        
        String permissionString = (String) permission;
        
        // Проверяем наличие права доступа
        return hasAuthority(authentication, permissionString);
    }
}
