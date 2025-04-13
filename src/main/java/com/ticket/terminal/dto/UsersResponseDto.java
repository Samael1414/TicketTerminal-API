package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDto {

    private String userName;
    private String role;
    private String fullName;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
}
