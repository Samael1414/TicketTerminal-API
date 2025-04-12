package com.ticket.terminal.dao;

import com.ticket.terminal.dto.GateInfoDto;

import java.util.List;

public interface GateInfoFacade {

    GateInfoDto findById(Long id);
    List<GateInfoDto> findAll();
    GateInfoDto save(GateInfoDto gateInfoDto);
    void delete(GateInfoDto gateInfoDto);
}
