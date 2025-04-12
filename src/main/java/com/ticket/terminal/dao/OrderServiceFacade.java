package com.ticket.terminal.dao;

import com.ticket.terminal.dto.OrderServiceDto;

import java.util.List;

public interface OrderServiceFacade {

    OrderServiceDto findById(Long id);
    List<OrderServiceDto> findAll();
    OrderServiceDto save(OrderServiceDto orderServiceDto);
    void delete(OrderServiceDto orderServiceDto);
}
