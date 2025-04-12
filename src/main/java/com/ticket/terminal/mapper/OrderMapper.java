package com.ticket.terminal.mapper;


import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.OrderEntity;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {OrderServiceMapper.class})
public interface OrderMapper {

    OrderDto toDto(OrderEntity orderEntity);

    OrderEntity toEntity(OrderDto orderDto);

    @Mapping(source = "id", target = "orderId")
    OrderCreateResponseDto toResponseDto(OrderEntity orderEntity);

    OrderEntity toEntity(SimpleOrderRequestDto dto);

    OrderEntity toEntity(EditableOrderRequestDto dto);

    @AfterMapping
    default void setDefault(@MappingTarget OrderEntity orderEntity, SimpleOrderRequestDto dto) {
        orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateOrderBarcode());
        orderEntity.setCreatedAt(LocalDateTime.now());
    }

    @AfterMapping
    default void setDefault(@MappingTarget OrderEntity orderEntity, EditableOrderRequestDto dto) {
        orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateOrderBarcode());
        orderEntity.setCreatedAt(LocalDateTime.now());
    }
}
