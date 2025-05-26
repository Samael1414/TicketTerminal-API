package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность для хранения прав доступа пользователя
 */
@Entity
@Getter
@Setter
@Table(name = "user_permissions")
public class UserPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(nullable = false)
    private Boolean canManageUsers = false;

    @Column(nullable = false)
    private Boolean canManageServices = false;

    @Column(nullable = false)
    private Boolean canManageCategories = false;

    @Column(nullable = false)
    private Boolean canManageVisitObjects = false;

    @Column(nullable = false)
    private Boolean canViewReports = false;

    @Column(nullable = false)
    private Boolean canManageSettings = false;

    @Column(nullable = false)
    private Boolean canManageOrders = false;

    @Column(nullable = false)
    private Boolean canExportData = false;

    @Column(nullable = false)
    private Boolean canImportData = false;
}
