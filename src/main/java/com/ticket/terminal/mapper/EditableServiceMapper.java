package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.EditableServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface EditableServiceMapper {

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "comment")
    @Mapping(source = "dtBegin", target = "dtBegin", qualifiedByName = "toDateString")
    @Mapping(source = "dtEnd", target = "dtEnd", qualifiedByName = "toDateString")
    EditableServiceDto toDto(ServiceEntity serviceEntity);

    @Mapping(source = "serviceId", target = "id")
    @Mapping(source = "comment", target = "description")
    ServiceEntity toEntity(EditableServiceDto dto);

    @Named("toDateString")
    static String toDateString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }
}
