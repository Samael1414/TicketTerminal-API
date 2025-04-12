package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.ServiceDto;
import com.ticket.terminal.dto.SimpleServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDto toDto(ServiceEntity serviceEntity);

    ServiceEntity toEntity(ServiceDto serviceDto);

    SimpleServiceDto toSimpleDto(ServiceEntity entity);
}
