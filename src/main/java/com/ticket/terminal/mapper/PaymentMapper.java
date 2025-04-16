package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.PaymentDto;
import com.ticket.terminal.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentDto toDto(PaymentEntity paymentEntity);

    @Mapping(target = "order.id", source = "orderId")
    PaymentEntity toEntity(PaymentDto paymentDto);
}
