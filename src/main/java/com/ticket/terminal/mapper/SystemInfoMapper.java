package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.SystemInfoDto;
import com.ticket.terminal.entity.SystemInfoEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SystemInfoMapper {

    SystemInfoDto toDto(SystemInfoEntity systemInfoEntity);

    SystemInfoEntity toEntity(SystemInfoDto systemInfoDto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SystemInfoDto dto, @MappingTarget SystemInfoEntity entity);
}
