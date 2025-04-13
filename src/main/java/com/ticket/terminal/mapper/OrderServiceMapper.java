package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.EditableOrderServiceDto;
import com.ticket.terminal.dto.OrderServiceDto;
import com.ticket.terminal.dto.SimpleOrderServiceDto;
import com.ticket.terminal.entity.OrderServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderServiceMapper {


    @Mapping(target = "orderServiceId", source = "id")
    @Mapping(target = "serviceStateName",
            expression = "java(com.ticket.terminal.enums.ServiceState.getNameByCode(entity.getServiceStateId()))")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.serviceName")
    OrderServiceDto toDto(OrderServiceEntity entity);

    @Mapping(target = "id", source = "orderServiceId")
    @Mapping(target = "service.id", source = "serviceId")
    OrderServiceEntity toEntity(OrderServiceDto orderServiceDto);

    OrderServiceEntity toEntity(SimpleOrderServiceDto simpleDto);


    @Mapping(target = "service.id", source = "serviceId")
    @Mapping(target = "cost", source = "serviceCost")
    OrderServiceEntity toEntity(EditableOrderServiceDto editableDto);
}
