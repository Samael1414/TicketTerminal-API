package com.ticket.terminal.dao;

import com.ticket.terminal.dto.PaymentDto;

import java.util.List;

public interface PaymentFacade {

    PaymentDto findById(Long id);
    List<PaymentDto> findAll();
    PaymentDto save(PaymentDto paymentDto);
    void delete(PaymentDto paymentDto);
}
