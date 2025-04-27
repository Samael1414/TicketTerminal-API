package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.dto.SeanceGridDto;
import com.ticket.terminal.entity.CategoryVisitorEntity;
import com.ticket.terminal.entity.SeanceGridEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SeanceGridMapper {


    SeanceGridDto toDto(SeanceGridEntity seanceGridEntity);

    SeanceGridEntity toEntity(SeanceGridDto seanceGridDto);

    List<SeanceGridDto> toDtoList(List<SeanceGridEntity> entities);
}
