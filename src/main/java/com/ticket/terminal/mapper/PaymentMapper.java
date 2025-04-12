package com.ticket.terminal.mapper;


import com.ticket.terminal.dto.PaymentDto;
import com.ticket.terminal.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(PaymentEntity paymentEntity);

    PaymentEntity toEntity(PaymentDto paymentDto);
}
