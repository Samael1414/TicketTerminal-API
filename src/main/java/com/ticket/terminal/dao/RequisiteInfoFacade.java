package com.ticket.terminal.dao;

import com.ticket.terminal.dto.RequisiteInfoDto;

import java.util.List;

public interface RequisiteInfoFacade {

    RequisiteInfoDto findById(Long id);
    List<RequisiteInfoDto> findAll();
    RequisiteInfoDto save(RequisiteInfoDto requisiteInfoDto);
    void delete(RequisiteInfoDto requisiteInfoDto);
}
