package com.ticket.terminal.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о пользователе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersResponseDto {

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("IsRoot")
    private Boolean isRoot;
    
    @JsonProperty("Permissions")
    private UserPermissionDto permissions;
}
