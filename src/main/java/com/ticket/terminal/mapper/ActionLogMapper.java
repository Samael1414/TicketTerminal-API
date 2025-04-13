package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.util.ActionLogFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ActionLogFactory.class)
public interface ActionLogMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "actorName", source = "actorName")
    ActionLogDto toDto(ActionLogEntity actionLogEntity);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "actorName", source = "actorName")
    ActionLogEntity toEntity(ActionLogDto actionLogDto);

}
