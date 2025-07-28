package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.service.ServiceCreateDto;
import com.ticket.terminal.dto.service.ServiceDto;
import com.ticket.terminal.dto.service.ServiceUpdateDto;
import com.ticket.terminal.dto.simple.SimpleServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    // Маппинг для /REST/Service/Editable
    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "dtBegin", target = "dtBegin", qualifiedByName = "formatDate")
    @Mapping(source = "dtEnd", target = "dtEnd", qualifiedByName = "formatDate")
    ServiceDto toDto(ServiceEntity serviceEntity);

    // Маппинг для /REST/Service/Simple
    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "comment")
    @Mapping(source = "dtBegin", target = "dtBegin", qualifiedByName = "formatDateTime")
    @Mapping(source = "dtEnd", target = "dtEnd", qualifiedByName = "formatDateTime")
    SimpleServiceDto toSimpleDto(ServiceEntity entity);

    ServiceEntity toEntity(ServiceCreateDto dto);

    void updateEntity(@MappingTarget ServiceEntity entity, ServiceUpdateDto dto);

    @Named("formatDate")
    default String formatDate(LocalDateTime dt) {
        return dt != null ? dt.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }
}
