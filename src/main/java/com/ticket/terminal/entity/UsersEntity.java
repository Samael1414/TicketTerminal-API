package com.ticket.terminal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Сущность пользователя системы
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private String fullName;

    private String phone;

    @Column(unique = true)
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdAt;
    
    /**
     * Признак, что пользователь является root-пользователем (имеет все права и не может быть изменен)
     */
    @Column(nullable = false)
    private Boolean isRoot = false;
    
    /**
     * Связь с правами доступа пользователя
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserPermissionEntity permissions;
}
