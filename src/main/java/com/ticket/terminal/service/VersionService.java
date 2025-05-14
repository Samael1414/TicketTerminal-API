package com.ticket.terminal.service;

import com.ticket.terminal.dto.GateInfoDto;
import com.ticket.terminal.dto.RequisiteInfoDto;
import com.ticket.terminal.dto.SystemInfoDto;
import com.ticket.terminal.dto.VersionDto;
import com.ticket.terminal.entity.GateInfoEntity;
import com.ticket.terminal.entity.RequisiteInfoEntity;
import com.ticket.terminal.entity.SystemInfoEntity;
import com.ticket.terminal.mapper.GateInfoMapper;
import com.ticket.terminal.mapper.RequisiteMapper;
import com.ticket.terminal.mapper.SystemInfoMapper;
import com.ticket.terminal.repository.GateInfoRepository;
import com.ticket.terminal.repository.RequisiteInfoRepository;
import com.ticket.terminal.repository.SystemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Stream;

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
        List<GateInfoDto> gates;
        try (Stream<GateInfoEntity> stream = gateInfoRepository.findAll().stream()) {
            gates = stream.map(gateInfoMapper::toDto).toList();
        }
        List<SystemInfoDto> systems;
        try (Stream<SystemInfoEntity> stream = systemInfoRepository.findAll().stream()) {
            systems = stream.map(systemInfoMapper::toDto).toList();
        }
        List<RequisiteInfoDto> requisites;
        try (Stream<RequisiteInfoEntity> stream = requisiteInfoRepository.findAll().stream()) {
            requisites = stream.map(requisiteMapper::toDto).toList();
        }
        return VersionDto
                .builder()
                .gate(gates)
                .system(systems)
                .requisite(requisites)
                .build();
    }
}
