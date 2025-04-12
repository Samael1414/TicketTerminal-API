package com.ticket.terminal.dao;

import com.ticket.terminal.dto.VisitObjectDto;

import java.util.List;

public interface VisitObjectFacade {

    VisitObjectDto findById(Long id);
    List<VisitObjectDto> findAll();
    VisitObjectDto save(VisitObjectDto visitObjectDto);
    void delete(VisitObjectDto visitObjectDto);
}
