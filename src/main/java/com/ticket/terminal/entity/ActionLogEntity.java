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
