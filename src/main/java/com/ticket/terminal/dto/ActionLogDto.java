package com.ticket.terminal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ActionLogDto {

    private Long userId;
    private String actionType;
    private String description;
    private LocalDateTime createdAt;
    private String actorName;
}
