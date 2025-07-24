package com.ticket.terminal.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи информации о правах доступа пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPermissionDto {

    @JsonProperty("CanManageUsers")
    private Boolean canManageUsers = false;

    @JsonProperty("CanManageServices")
    private Boolean canManageServices = false;

    @JsonProperty("CanManageCategories")
    private Boolean canManageCategories = false;

    @JsonProperty("CanManageVisitObjects")
    private Boolean canManageVisitObjects = false;

    @JsonProperty("CanViewReports")
    private Boolean canViewReports = false;

    @JsonProperty("CanManageSettings")
    private Boolean canManageSettings = false;

    @JsonProperty("CanManageOrders")
    private Boolean canManageOrders = false;

    @JsonProperty("CanExportData")
    private Boolean canExportData = false;

    @JsonProperty("CanImportData")
    private Boolean canImportData = false;
}
