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
import org.springframework.transaction.annotation.Transactional;

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

        versionDto.getGate().stream().findFirst().ifPresent(dto -> {
            GateInfoEntity current = gateInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Gate info not found"));
            boolean changed = false;
            if (dto.getName() != null && !dto.getName().equals(current.getName())) {
                current.setName(dto.getName()); changed = true;
            }
            if (dto.getVersion() != null && !dto.getVersion().equals(current.getVersion())) {
                current.setVersion(dto.getVersion()); changed = true;
            }
            if (dto.getMajor() != null && !dto.getMajor().equals(current.getMajor())) {
                current.setMajor(dto.getMajor()); changed = true;
            }
            if (dto.getMinor() != null && !dto.getMinor().equals(current.getMinor())) {
                current.setMinor(dto.getMinor()); changed = true;
            }
            if (dto.getRelease() != null && !dto.getRelease().equals(current.getRelease())) {
                current.setRelease(dto.getRelease()); changed = true;
            }
            if (dto.getBuild() != null && !dto.getBuild().equals(current.getBuild())) {
                current.setBuild(dto.getBuild()); changed = true;
            }
            if (dto.getDtLicenceFinish() != null && !dto.getDtLicenceFinish().equals(current.getDtLicenceFinish())) {
                current.setDtLicenceFinish(dto.getDtLicenceFinish()); changed = true;
            }
            if (changed) gateInfoRepository.save(current);
        });

        versionDto.getSystem().stream().findFirst().ifPresent(dto -> {
            SystemInfoEntity current = systemInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("System info not found"));
            boolean changed = false;
            if (dto.getName() != null && !dto.getName().equals(current.getName())) {
                current.setName(dto.getName()); changed = true;
            }
            if (dto.getVersion() != null && !dto.getVersion().equals(current.getVersion())) {
                current.setVersion(dto.getVersion()); changed = true;
            }
            if (dto.getMajor() != null && !dto.getMajor().equals(current.getMajor())) {
                current.setMajor(dto.getMajor()); changed = true;
            }
            if (dto.getMinor() != null && !dto.getMinor().equals(current.getMinor())) {
                current.setMinor(dto.getMinor()); changed = true;
            }
            if (dto.getRelease() != null && !dto.getRelease().equals(current.getRelease())) {
                current.setRelease(dto.getRelease()); changed = true;
            }
            if (dto.getBuild() != null && !dto.getBuild().equals(current.getBuild())) {
                current.setBuild(dto.getBuild()); changed = true;
            }
            if (dto.getDtLicenceFinish() != null && !dto.getDtLicenceFinish().equals(current.getDtLicenceFinish())) {
                current.setDtLicenceFinish(dto.getDtLicenceFinish()); changed = true;
            }
            if (changed) systemInfoRepository.save(current);
        });

        versionDto.getRequisite().stream().findFirst().ifPresent(dto -> {
            RequisiteInfoEntity current = requisiteInfoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Requisite info not found"));
            boolean changed = false;
            if (dto.getName() != null && !dto.getName().equals(current.getName())) {
                current.setName(dto.getName()); changed = true;
            }
            if (dto.getCity() != null && !dto.getCity().equals(current.getCity())) {
                current.setCity(dto.getCity()); changed = true;
            }
            if (dto.getAddress() != null && !dto.getAddress().equals(current.getAddress())) {
                current.setAddress(dto.getAddress()); changed = true;
            }
            if (dto.getPhone1() != null && !dto.getPhone1().equals(current.getPhone1())) {
                current.setPhone1(dto.getPhone1()); changed = true;
            }
            if (dto.getFax() != null && !dto.getFax().equals(current.getFax())) {
                current.setFax(dto.getFax()); changed = true;
            }
            if (dto.getDtBegin() != null && !dto.getDtBegin().equals(current.getDtBegin())) {
                current.setDtBegin(dto.getDtBegin()); changed = true;
            }
            if (dto.getDtEnd() != null && !dto.getDtEnd().equals(current.getDtEnd())) {
                current.setDtEnd(dto.getDtEnd()); changed = true;
            }
            if (changed) requisiteInfoRepository.save(current);
        });
    }


}
