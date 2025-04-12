package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.SystemInfoDto;
import com.ticket.terminal.entity.SystemInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemInfoMapper {

    SystemInfoDto toDto(SystemInfoEntity systemInfoEntity);

    SystemInfoEntity toEntity(SystemInfoDto systemInfoDto);
}
