package com.ticket.terminal.service;

import com.ticket.terminal.dto.OrganizationDto;
import com.ticket.terminal.mapper.OrganizationMapper;
import com.ticket.terminal.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public List<OrganizationDto> getAllOrganization() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toDto)
                .toList();
    }

}
