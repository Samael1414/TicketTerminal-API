package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersCreateDto {

    private String userName;
    private String password;
    private String role;
    private String fullName;
    private String phone;
    private String email;
}
