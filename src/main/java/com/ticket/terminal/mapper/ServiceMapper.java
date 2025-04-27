package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.ServiceDto;
import com.ticket.terminal.dto.SimpleServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "description")
    ServiceDto toDto(ServiceEntity serviceEntity);

    ServiceEntity toEntity(ServiceDto serviceDto);

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "comment")
    SimpleServiceDto toSimpleDto(ServiceEntity entity);
}
