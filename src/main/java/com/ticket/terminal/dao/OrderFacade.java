package com.ticket.terminal.dao;

import com.ticket.terminal.dto.OrderDto;

import java.util.List;

public interface OrderFacade {

    OrderDto findById(Long id);
    List<OrderDto> findAll();
    OrderDto save(OrderDto orderDto);
    void delete(OrderDto orderDto);
}
