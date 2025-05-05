package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.dto.SeanceGridDto;
import com.ticket.terminal.entity.CategoryVisitorEntity;
import com.ticket.terminal.entity.SeanceGridEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SeanceGridMapper {

    @Mapping(source = "dtBegin", target = "dtBegin", qualifiedByName = "timeToString")
    @Mapping(source = "dtEnd",   target = "dtEnd",   qualifiedByName = "timeToString")
    SeanceGridDto toDto(SeanceGridEntity entity);

    List<SeanceGridDto> toDtoList(List<SeanceGridEntity> entities);

    @Named("timeToString")
    default String timeToString(LocalTime t) {
        return t.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}

