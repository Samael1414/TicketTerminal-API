package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.EditableServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditableServiceMapper {

    EditableServiceDto toDto(ServiceEntity serviceEntity);

    ServiceEntity toEntity(EditableServiceDto editableServiceDto);
}
