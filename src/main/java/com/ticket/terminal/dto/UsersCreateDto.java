package com.ticket.terminal.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersCreateDto {

    private String userName;
    private String password;
    private String role;
    private String fullName;
    private String phone;
    private String email;
}
