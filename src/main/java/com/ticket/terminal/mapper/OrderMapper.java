package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.editable.EditableOrderRequestDto;
import com.ticket.terminal.dto.order.OrderCreateResponseDto;
import com.ticket.terminal.dto.order.OrderDto;
import com.ticket.terminal.dto.simple.SimpleOrderRequestDto;
import com.ticket.terminal.entity.order.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {OrderServiceMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "service",   defaultExpression = "java(Collections.emptyList())")
    OrderDto toDto(OrderEntity orderEntity);

    @Mapping(target = "orderBarcode", ignore = true)
    OrderEntity toEntity(OrderDto orderDto);

    @Mapping(source = "id", target = "orderId")
    @Mapping(target = "soldRequest", ignore = true)
    OrderCreateResponseDto toResponseDto(OrderEntity orderEntity);


    @Mapping(target = "orderBarcode", ignore = true)
    OrderEntity toEntity(SimpleOrderRequestDto dto);


    @Mapping(target = "orderBarcode", ignore = true)
    OrderEntity toEntity(EditableOrderRequestDto dto);

}
