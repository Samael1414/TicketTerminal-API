package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер для преобразования между DTO и сущностями пользователей
 */
@Mapper(componentModel = "spring", uses = {UserPermissionMapper.class})
public interface UsersMapper {

    /**
     * Преобразует сущность в DTO ответа
     */
    UsersResponseDto toDto(UsersEntity usersEntity);

    /**
     * Преобразует DTO создания в сущность
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    UsersEntity toEntity(UsersCreateDto usersCreateDto);
    
    /**
     * Обновляет существующую сущность данными из DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateEntityFromDto(UsersCreateDto dto, @MappingTarget UsersEntity entity);
}
