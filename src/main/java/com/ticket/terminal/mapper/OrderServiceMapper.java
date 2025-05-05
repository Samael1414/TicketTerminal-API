package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.EditableOrderServiceDto;
import com.ticket.terminal.dto.OrderServiceDto;
import com.ticket.terminal.dto.SimpleOrderServiceDto;
import com.ticket.terminal.entity.OrderServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface OrderServiceMapper {

    @Mapping(target = "orderServiceId", source = "id")
    @Mapping(target = "serviceStateName",
            expression = "java(com.ticket.terminal.enums.ServiceState.getNameByCode(entity.getServiceStateId()))")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.serviceName")
    @Mapping(target = "dtVisit", expression = "java(toZoned(entity.getDtVisit()))")
    OrderServiceDto toDto(OrderServiceEntity entity);

    @Mapping(target = "id", source = "orderServiceId")
    @Mapping(target = "service.id", source = "serviceId")
    @Mapping(target = "dtVisit", expression = "java(toLocal(orderServiceDto.getDtVisit()))")
    @Mapping(target = "order", ignore = true)
    OrderServiceEntity toEntity(OrderServiceDto orderServiceDto);

    @Mapping(target = "dtVisit", expression = "java(toLocal(simpleDto.getDtVisit()))")
    @Mapping(target = "order", ignore = true)
    OrderServiceEntity toEntity(SimpleOrderServiceDto simpleDto);

    @Mapping(target = "service.id", source = "serviceId")
    @Mapping(target = "cost", source = "serviceCost")
    @Mapping(target = "dtVisit", expression = "java(toLocal(editableDto.getDtVisit()))")
    @Mapping(target = "order", ignore = true)
    OrderServiceEntity toEntity(EditableOrderServiceDto editableDto);

    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceCost", source = "cost")
    @Mapping(target = "dtVisit", expression = "java(toZoned(entity.getDtVisit()))")
    @Mapping(target = "dtBegin", expression = "java(formatDate(entity.getDtVisit()))")
    @Mapping(target = "dtEnd", expression = "java(formatDate(entity.getDtDrop()))")
    EditableOrderServiceDto toEditableDto(OrderServiceEntity entity);


    default ZonedDateTime toZoned(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.of("Europe/Moscow")) : null;
    }

    default java.time.LocalDateTime toLocal(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }

    default String formatDate(LocalDateTime dt) {
        return dt != null ? dt.toLocalDate().toString() : null;
    }

}
