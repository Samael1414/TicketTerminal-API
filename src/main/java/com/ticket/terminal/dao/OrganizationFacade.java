package com.ticket.terminal.dao;

import com.ticket.terminal.dto.OrganizationDto;

import java.util.List;

public interface OrganizationFacade {

    OrganizationDto findById(long id);
    List<OrganizationDto> findAll();
    OrganizationDto save(OrganizationDto organizationDto);
    void delete(OrganizationDto organizationDto);
}
