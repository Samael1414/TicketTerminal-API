package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.SoldServiceDto;
import com.ticket.terminal.entity.SoldServiceEntity;
import com.ticket.terminal.mapper.impl.VisitObjectIdMapper;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring", uses = VisitObjectIdMapper.class)
public interface SoldServiceMapper {

    @Mappings({
            @Mapping(target = "visitObjectId", source = "visitObject",
                    qualifiedByName = "mapVisitObjectsToIds"),
            @Mapping(target = "visitObject",   source = "visitObject",
                    qualifiedByName = "mapVisitObjectsToIds"),
            @Mapping(target = "serviceId", source = "orderService.service.id"),
            @Mapping(source = "orderServiceId", target = "orderServiceId"),
            @Mapping(source = "barcode",        target = "barcode"),
            @Mapping(source = "serviceStateId", target = "serviceStateId"),
            @Mapping(source = "paymentKindId",  target = "paymentKindId"),
            @Mapping(source = "dtActive",       target = "dtActive"),
            @Mapping(source = "serviceCost",    target = "serviceCost"),
            @Mapping(source = "serviceCount",   target = "serviceCount"),
            @Mapping(source = "orderService.dtVisit", target = "dtVisit")
    })
    SoldServiceDto toDto(SoldServiceEntity entity);

    @Mappings({
            @Mapping(target = "visitObject",
                    source  = "visitObjectId",
                    qualifiedByName = "mapIdsToVisitObjects"),
            @Mapping(source = "serviceId", target = "serviceId"),
            @Mapping(target = "orderService", ignore = true)
    })
    SoldServiceEntity toEntity(SoldServiceDto dto);

    List<SoldServiceDto> toDtoList(List<SoldServiceEntity> entities);

    @AfterMapping
    default void fillMissingServiceId(@MappingTarget SoldServiceDto dto, SoldServiceEntity entity) {
        if (dto.getServiceId() == null &&
                entity.getOrderService() != null &&
                entity.getOrderService().getService() != null) {
            dto.setServiceId(entity.getOrderService().getService().getId());
        }
    }
}
