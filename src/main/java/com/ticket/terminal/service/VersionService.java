package com.ticket.terminal.service;


import com.ticket.terminal.dto.GateInfoDto;
import com.ticket.terminal.dto.RequisiteInfoDto;
import com.ticket.terminal.dto.SystemInfoDto;
import com.ticket.terminal.dto.VersionDto;
import com.ticket.terminal.mapper.GateInfoMapper;
import com.ticket.terminal.mapper.RequisiteMapper;
import com.ticket.terminal.mapper.SystemInfoMapper;
import com.ticket.terminal.repository.GateInfoRepository;
import com.ticket.terminal.repository.RequisiteInfoRepository;
import com.ticket.terminal.repository.SystemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final GateInfoRepository gateInfoRepository;
    private final SystemInfoRepository systemInfoRepository;
    private final RequisiteInfoRepository requisiteInfoRepository;

    private final GateInfoMapper gateInfoMapper;
    private final SystemInfoMapper systemInfoMapper;
    private final RequisiteMapper requisiteMapper;


    public VersionDto getAllVersion() {
        List<GateInfoDto> gates = gateInfoRepository.findAll().stream()
                .map(gateInfoMapper::toDto)
                .toList();
        List<SystemInfoDto> systems = systemInfoRepository.findAll().stream()
                .map(systemInfoMapper::toDto)
                .toList();
        List<RequisiteInfoDto> requisites = requisiteInfoRepository.findAll().stream()
                .map(requisiteMapper::toDto)
                .toList();

        VersionDto versionDto = new VersionDto();
        versionDto.setGate(gates);
        versionDto.setSystem(systems);
        versionDto.setRequisite(requisites);
        return versionDto;
    }
}
