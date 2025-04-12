package com.ticket.terminal.dao;

import com.ticket.terminal.dto.SystemInfoDto;

import java.util.List;

public interface SystemInfoFacade {

    SystemInfoDto findById(long id);
    List<SystemInfoDto> findAll();
    SystemInfoDto save(SystemInfoDto systemInfoDto);
    void deleteById(long id);
}
