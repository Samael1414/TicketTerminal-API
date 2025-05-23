package com.ticket.terminal.service;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.mapper.ActionLogMapper;
import com.ticket.terminal.repository.ActionLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final ActionLogMapper actionLogMapper;

    public void save(ActionLogEntity entity) {
        // Убедимся, что id не установлен, чтобы использовать автоинкремент
        entity.setId(null);
        actionLogRepository.save(entity);
    }

    public List<ActionLogDto> findAll() {
        try (Stream<ActionLogEntity> stream = actionLogRepository.findAll().stream()) {
            return stream
                    .map(actionLogMapper::toDto)
                    .toList();
        }
    }

    @Transactional
    public void deleteLogsRange() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(120);
        actionLogRepository.deleteOlderThan(threshold);
    }
}
