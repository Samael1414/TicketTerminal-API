package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.UserPermissionDto;
import com.ticket.terminal.entity.UserPermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер для преобразования между DTO и сущностями прав доступа пользователя
 */
@Mapper(componentModel = "spring")
public interface UserPermissionMapper {

    /**
     * Преобразует DTO в сущность
     * Примечание: поля id и user должны устанавливаться вручную
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserPermissionEntity toEntity(UserPermissionDto dto);

    /**
     * Преобразует сущность в DTO
     */
    UserPermissionDto toDto(UserPermissionEntity entity);

    /**
     * Обновляет существующую сущность данными из DTO
     * Примечание: поля id и user не изменяются
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserPermissionDto dto, @MappingTarget UserPermissionEntity entity);
}
