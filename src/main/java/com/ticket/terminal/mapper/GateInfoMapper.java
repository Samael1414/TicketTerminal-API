package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.GateInfoDto;
import com.ticket.terminal.entity.GateInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GateInfoMapper {

    GateInfoDto toDto(GateInfoEntity gateInfoEntity);

    GateInfoEntity toEntity(GateInfoDto gateInfoDto);
}
