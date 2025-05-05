/**
 * Сущность (Entity) для хранения информации о действиях пользователей (Action Log) в базе данных.
 * 
 * Назначение:
 * - Представляет таблицу action_logs для аудита и логирования действий пользователей.
 * - Используется в сервисах, репозиториях и для построения отчётов/журналов.
 * 
 * Содержит данные:
 * - id (уникальный идентификатор)
 * - user (пользователь, связанный с действием)
 * - actionType (тип действия)
 * - description (описание действия)
 * - createdAt (дата и время действия)
 */
package com.ticket.terminal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "action_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private UsersEntity user;

    @Column(nullable = false)
    private String actionType;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdAt;

    private String actorName;
}
