package com.ticket.terminal.dao;

import com.ticket.terminal.dto.ServiceDto;

import java.util.List;

public interface ServiceFacade {

    ServiceDto findById(Long id);
    List<ServiceDto> findAll();
    ServiceDto save(ServiceDto serviceDto);
    void deleteById(Long id);
}
