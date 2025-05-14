package com.ticket.terminal.mapper.impl;

import com.ticket.terminal.dto.VisitObjectItemDto;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.repository.VisitObjectRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@RequiredArgsConstructor
public class VisitObjectIdMapper {

    private final VisitObjectRepository visitObjectRepository;

    @Named("mapVisitObjectsToIds")
    public List<Long> mapVisitObjectsToIds(List<VisitObjectEntity> entities) {
        return entities == null ? null :
                entities.stream()
                        .map(VisitObjectEntity::getId)
                        .toList();
    }

//    @Named("mapVisitObjectsToDto")
//    public List<VisitObjectItemDto> mapVisitObjectsToDto(List<VisitObjectEntity> entities) {
//        return entities == null ? null :
//                entities.stream()
//                        .map(e -> VisitObjectItemDto.builder()
//                                .visitObjectId(e.getId())
//                                .visitObjectName(e.getVisitObjectName())
//                                .isRequire(e.getIsRequire())
//                                .categoryVisitorId(e.getCategoryVisitorId())
//                                .address(e.getAddress())
//                                .comment(e.getComment())
//                                .build())
//                        .toList();
//    }


    @Named("mapIdsToVisitObjects")
    public List<VisitObjectEntity> mapIdsToVisitObjects(List<Long> ids) {
        return ids == null ? null : visitObjectRepository.findAllById(ids);
    }
}

