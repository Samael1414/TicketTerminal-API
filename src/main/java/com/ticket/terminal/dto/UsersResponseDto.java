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
public class UsersResponseDto {

    private String userName;
    private String role;
    private String fullName;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
}
