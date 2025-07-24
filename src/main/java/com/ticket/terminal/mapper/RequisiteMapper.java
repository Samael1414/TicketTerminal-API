package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.RequisiteInfoDto;
import com.ticket.terminal.entity.RequisiteInfoEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RequisiteMapper {

    RequisiteInfoDto toDto (RequisiteInfoEntity RequisiteInfoEntity);


    RequisiteInfoEntity toEntity (RequisiteInfoDto requisiteInfoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(RequisiteInfoDto dto, @MappingTarget RequisiteInfoEntity entity);
}
