package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogDto {

    private Long userId;
    private String actionType;
    private String description;
    private LocalDateTime createdAt;
    private String actorName;
}
