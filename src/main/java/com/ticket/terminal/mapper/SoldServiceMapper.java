package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.SoldServiceDto;
import com.ticket.terminal.entity.SoldServiceEntity;
import com.ticket.terminal.mapper.impl.VisitObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VisitObjectIdMapper.class})
public interface SoldServiceMapper {

    @Mapping(target = "visitObjectId", source = "visitObject", qualifiedByName = "mapVisitObjectToIds")
    @Mapping(target = "visitObject", source = "visitObject", qualifiedByName = "mapVisitObjectToIds")
    @Mapping(source = "orderServiceId", target = "orderServiceId")
    @Mapping(source = "barcode",         target = "barcode")
    @Mapping(source = "serviceStateId",  target = "serviceStateId")
    @Mapping(source = "paymentKindId",   target = "paymentKindId")
    @Mapping(source = "dtActive",        target = "dtActive")
    @Mapping(source = "serviceCost",     target = "serviceCost")
    @Mapping(source = "serviceCount",    target = "serviceCount")
    @Mapping(source = "orderService.service.id",  target = "serviceId")
    @Mapping(source = "orderService.dtVisit",     target = "dtVisit")
    SoldServiceDto toDto(SoldServiceEntity entity);

    List<SoldServiceDto> toDto(List<SoldServiceEntity> entity);

    @Mapping(target = "visitObject", source = "visitObject", qualifiedByName = "mapIdsToVisitObjects")
    SoldServiceEntity toEntity(SoldServiceDto dto);
}
