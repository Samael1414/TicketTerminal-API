package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionLogDto {

    private Integer userId;
    private String actionType;
    private String description;
    private LocalDateTime createdAt;
    private String actorName;
}
