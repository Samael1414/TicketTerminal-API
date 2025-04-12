package com.ticket.terminal.mapper;


import com.ticket.terminal.dto.SeanceGridDto;
import com.ticket.terminal.entity.SeanceGridEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeanceGridMapper {

    SeanceGridDto toDto(SeanceGridEntity seanceGridEntity);

    SeanceGridEntity toEntity(SeanceGridDto seanceGridDto);
}
