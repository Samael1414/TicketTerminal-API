package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {OrderServiceMapper.class})
public interface OrderMapper {

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

//    @AfterMapping
//    default void afterMappingSimple(@MappingTarget OrderEntity orderEntity, SimpleOrderRequestDto dto) {
//        OrderEntityUtil.initialize(orderEntity);
//    }

//    @AfterMapping
//    default void afterMappingEditable(@MappingTarget OrderEntity orderEntity, EditableOrderRequestDto dto) {
//        OrderEntityUtil.initialize(orderEntity);
//    }

}
