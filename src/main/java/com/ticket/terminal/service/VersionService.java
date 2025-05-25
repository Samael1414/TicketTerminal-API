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
     * Обновляет данные таблиц gate_info, system_info и requisite_info.
     * Использует первый найденный элемент каждой таблицы (если они существуют).
     * Заменяет старую сущность новой, с сохранением идентификатора.
     *
     * Обязательные поля:
     *   - GateInfo: name, version, major, minor, release, build, dtLicenceFinish
     *   - SystemInfo: name, version, major, minor, release, build, dtLicenceFinish
     *   - RequisiteInfo: name (остальные необязательны)
     *
     * @param versionDto — DTO с новыми значениями для всех трёх таблиц
     */
    public void updateAllVersion(VersionDto versionDto) {
        // Обновление gate_info
        try (Stream<GateInfoEntity> stream = gateInfoRepository.findAll().stream()) {
            versionDto.getGate().stream().findFirst().ifPresent(dto -> {
                // Получаем текущую запись из таблицы gate_info
                GateInfoEntity current = stream.findFirst()
                        .orElseThrow(() -> new IllegalStateException("Gate info not found"));
                // Преобразуем DTO в сущность и сохраняем id текущей записи
                GateInfoEntity updated = gateInfoMapper.toEntity(dto);
                updated.setId(current.getId());
                // Обязательные поля: name, version, major, minor, release, build, dtLicenceFinish
                gateInfoRepository.save(updated);
            });
        }

        // Обновление system_info
        try (Stream<SystemInfoEntity> stream = systemInfoRepository.findAll().stream()) {
            versionDto.getSystem().stream().findFirst().ifPresent(dto -> {
                // Получаем текущую запись из таблицы system_info
                SystemInfoEntity current = stream.findFirst()
                        .orElseThrow(() -> new IllegalStateException("System info not found"));
                // Преобразуем DTO в сущность и сохраняем id текущей записи
                SystemInfoEntity updated = systemInfoMapper.toEntity(dto);
                updated.setId(current.getId());
                // Обязательные поля: name, version, major, minor, release, build, dtLicenceFinish
                systemInfoRepository.save(updated);
            });
        }

        // Обновление requisite_info
        try (Stream<RequisiteInfoEntity> stream = requisiteInfoRepository.findAll().stream()) {
            versionDto.getRequisite().stream().findFirst().ifPresent(dto -> {
                // Получаем текущую запись из таблицы requisite_info
                RequisiteInfoEntity current = stream.findFirst()
                        .orElseThrow(() -> new IllegalStateException("Requisite info not found"));
                // Преобразуем DTO в сущность и сохраняем id текущей записи
                RequisiteInfoEntity updated = requisiteMapper.toEntity(dto);
                updated.setId(current.getId());
                // Обязательное поле: name (city, address, phone1, fax — необязательные)
                requisiteInfoRepository.save(updated);
            });
        }
    }
}
