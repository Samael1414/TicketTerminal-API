package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.entity.UsersEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersResponseDto toDto(UsersEntity usersEntity);

    UsersEntity toEntity(UsersCreateDto usersCreateDto);

}
