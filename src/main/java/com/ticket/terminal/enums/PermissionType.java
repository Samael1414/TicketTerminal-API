package com.ticket.terminal.enums;

import com.ticket.terminal.entity.UserPermissionEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.function.Predicate;

public enum PermissionType {
    CAN_MANAGE_USERS(UserPermissionEntity::getCanManageUsers),
    CAN_MANAGE_SERVICES(UserPermissionEntity::getCanManageServices),
    CAN_MANAGE_CATEGORIES(UserPermissionEntity::getCanManageCategories),
    CAN_MANAGE_VISIT_OBJECTS(UserPermissionEntity::getCanManageVisitObjects),
    CAN_VIEW_REPORTS(UserPermissionEntity::getCanViewReports),
    CAN_MANAGE_SETTINGS(UserPermissionEntity::getCanManageSettings),
    CAN_MANAGE_ORDERS(UserPermissionEntity::getCanManageOrders),
    CAN_EXPORT_DATA(UserPermissionEntity::getCanExportData),
    CAN_IMPORT_DATA(UserPermissionEntity::getCanImportData);

    private final Predicate<UserPermissionEntity> check;

    PermissionType(Predicate<UserPermissionEntity> check) {
        this.check = check;
    }

    /**
     * true, если perms не null и флаг из perms равен TRUE
     */
    public boolean isEnabled(UserPermissionEntity perms) {
        return perms != null && Boolean.TRUE.equals(check.test(perms));
    }

    /**
     * Компактное имя права без префикса
     */
    public String authority() {
        return name();
    }

    /**
     * SimpleGrantedAuthority для этого права
     */
    public SimpleGrantedAuthority grantedAuthority() {
        return new SimpleGrantedAuthority(authority());
    }
}
