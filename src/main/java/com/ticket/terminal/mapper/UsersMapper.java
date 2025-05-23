package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersResponseDto toDto(UsersEntity usersEntity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UsersEntity toEntity(UsersCreateDto usersCreateDto);

}
