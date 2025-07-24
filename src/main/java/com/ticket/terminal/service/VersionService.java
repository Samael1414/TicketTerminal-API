package com.ticket.terminal.service;

import com.ticket.terminal.dto.GateInfoDto;
import com.ticket.terminal.dto.RequisiteInfoDto;
import com.ticket.terminal.dto.SystemInfoDto;
import com.ticket.terminal.dto.VersionDto;
import com.ticket.terminal.entity.GateInfoEntity;
import com.ticket.terminal.entity.RequisiteInfoEntity;
import com.ticket.terminal.entity.SystemInfoEntity;
import com.ticket.terminal.exception.EntityNotFoundException;
import com.ticket.terminal.mapper.GateInfoMapper;
import com.ticket.terminal.mapper.RequisiteMapper;
import com.ticket.terminal.mapper.SystemInfoMapper;
import com.ticket.terminal.repository.GateInfoRepository;
import com.ticket.terminal.repository.RequisiteInfoRepository;
import com.ticket.terminal.repository.SystemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VersionService {

    private final GateInfoRepository gateInfoRepository;
    private final SystemInfoRepository systemInfoRepository;
    private final RequisiteInfoRepository requisiteInfoRepository;

    private final GateInfoMapper gateInfoMapper;
    private final SystemInfoMapper systemInfoMapper;
    private final RequisiteMapper requisiteMapper;

    /**
     * Получение полной информации о версиях gate, system и requisites.
     * Возвращает DTO с данными всех таблиц.
     */
    public VersionDto getAllVersion() {
        List<GateInfoDto> gates;
        try (Stream<GateInfoEntity> stream = gateInfoRepository.findAll().stream()) {
            // Преобразуем сущности gate_info в DTO
            gates = stream.map(gateInfoMapper::toDto).toList();
        }

        List<SystemInfoDto> systems;
        try (Stream<SystemInfoEntity> stream = systemInfoRepository.findAll().stream()) {
            // Преобразуем сущности system_info в DTO
            systems = stream.map(systemInfoMapper::toDto).toList();
        }

        List<RequisiteInfoDto> requisites;
        try (Stream<RequisiteInfoEntity> stream = requisiteInfoRepository.findAll().stream()) {
            // Преобразуем сущности requisite_info в DTO
            requisites = stream.map(requisiteMapper::toDto).toList();
        }

        // Формируем итоговый объект, объединяя все версии
        return VersionDto
                .builder()
                .gate(gates)
                .system(systems)
                .requisite(requisites)
                .build();
    }


    /**
     * Обновляет существующие записи в таблицах gate_info, system_info и requisite_info,
     * если DTO содержит новые значения (не null).
     * Старые значения не затираются, если соответствующее поле в DTO не задано.
     * Предполагается, что в каждой таблице содержится только одна строка.
     */
    @Transactional
    public void updateAllVersion(VersionDto versionDto) {
        // gate_info
        versionDto.getGate().stream().findFirst().ifPresent(dto -> {
            GateInfoEntity gateInfoEntity = gateInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Gate info not found"));
            gateInfoMapper.updateFromDto(dto, gateInfoEntity);
            gateInfoRepository.save(gateInfoEntity);
        });

        // system_info
        versionDto.getSystem().stream().findFirst().ifPresent(dto -> {
            SystemInfoEntity systemInfoEntity = systemInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("System info not found"));
            systemInfoMapper.updateFromDto(dto, systemInfoEntity);
            systemInfoRepository.save(systemInfoEntity);
        });

        // requisite_info
        versionDto.getRequisite().stream().findFirst().ifPresent(dto -> {
            RequisiteInfoEntity requisiteInfoEntity = requisiteInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Requisite info not found"));
            requisiteMapper.updateFromDto(dto, requisiteInfoEntity);
            requisiteInfoRepository.save(requisiteInfoEntity);
        });
    }


}
