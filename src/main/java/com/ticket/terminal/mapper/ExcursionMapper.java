package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.ExcursionDto;
import com.ticket.terminal.dto.ExcursionRequestDto;
import com.ticket.terminal.dto.ExcursionResponseDto;
import com.ticket.terminal.entity.ExcursionLogEntity;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ExcursionMapper {

    @Mapping(source = "id", target = "serviceId")
    ExcursionDto toDto(ServiceEntity serviceEntity);


    @Mapping(target = "service.id", source = "serviceId")
    ExcursionLogEntity toEntity(ExcursionRequestDto dto);


    @Mapping(target = "excursionLogId", source = "id")
    ExcursionResponseDto toDto(ExcursionLogEntity entity);

    @AfterMapping
    default void fillCreatedAt(@MappingTarget ExcursionLogEntity entity, ExcursionRequestDto dto) {
        entity.setCreatedAt(LocalDateTime.now());
    }

}
