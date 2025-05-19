package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.entity.VisitObjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitObjectMapper {

    @Mapping(source = "id", target = "visitObjectId")
    @Mapping(source = "isRequire", target = "isRequire")
    @Mapping(source = "visitObjectName", target = "visitObjectName")
    @Mapping(source = "id", target = "groupVisitObjectId")
    VisitObjectDto toDto(VisitObjectEntity visitObjectEntity);

    @Mapping(target = "id", ignore = true)
    VisitObjectEntity toEntity(VisitObjectDto visitObjectDto);

    @Mapping(source = "id", target = "visitObjectId")
    @Mapping(source = "isRequire", target = "isRequire")
    @Mapping(source = "visitObjectName", target = "visitObjectName")
    List<VisitObjectDto> toDtoList(List<VisitObjectEntity> entities);
}
