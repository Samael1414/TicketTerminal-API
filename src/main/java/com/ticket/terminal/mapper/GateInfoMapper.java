package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.GateInfoDto;
import com.ticket.terminal.entity.GateInfoEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GateInfoMapper {

    GateInfoDto toDto(GateInfoEntity gateInfoEntity);

    GateInfoEntity toEntity(GateInfoDto gateInfoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(GateInfoDto dto, @MappingTarget GateInfoEntity entity);
}
