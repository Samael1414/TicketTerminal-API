package com.ticket.terminal.dao;

import com.ticket.terminal.dto.ActionLogDto;

import java.util.List;

public interface ActionLogFacade {

    ActionLogDto findById(Long id);
    List<ActionLogDto> findAll();
    ActionLogDto save(ActionLogDto actionLogDto);
    void deleteById(Long id);
}
