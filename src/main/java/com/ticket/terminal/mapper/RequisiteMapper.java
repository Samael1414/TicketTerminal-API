package com.ticket.terminal.mapper;


import com.ticket.terminal.dto.RequisiteInfoDto;
import com.ticket.terminal.entity.RequisiteInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequisiteMapper {

    RequisiteInfoDto toDto (RequisiteInfoEntity RequisiteInfoEntity);


    RequisiteInfoEntity toEntity (RequisiteInfoDto requisiteInfoDto);
}
