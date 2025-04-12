package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.OrganizationDto;
import com.ticket.terminal.entity.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {


    OrganizationDto toDto(OrganizationEntity organizationEntity);

    OrganizationEntity toEntity(OrganizationDto organizationDto);
}
