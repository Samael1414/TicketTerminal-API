package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.entity.VisitObjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitObjectMapper {

    @Mapping(source = "id", target = "visitObjectId")
    @Mapping(source = "required", target = "required")
    VisitObjectDto toDto(VisitObjectEntity visitObjectEntity);

    VisitObjectEntity toEntity(VisitObjectDto visitObjectDto);

    List<VisitObjectDto> toDtoList(List<VisitObjectEntity> entities);
}
