package com.ticket.terminal.mapper.impl;

import com.ticket.terminal.entity.VisitObjectEntity;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VisitObjectIdMapper {

    @Named("mapVisitObjectToIds")
    public List<Long> mapVisitObjectToIds(List<VisitObjectEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(VisitObjectEntity::getId).toList();
    }

    @Named("mapIdsToVisitObjects")
    public List<VisitObjectEntity> mapIdsToVisitObjects(List<Long> ids) {
        if (ids == null) return List.of();
        return ids.stream().map(id -> {
            VisitObjectEntity obj = new VisitObjectEntity();
            obj.setId(id);
            return obj;
        }).toList();
    }
}

