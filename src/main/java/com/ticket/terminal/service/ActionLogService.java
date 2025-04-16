package com.ticket.terminal.service;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.mapper.ActionLogMapper;
import com.ticket.terminal.repository.ActionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final ActionLogMapper actionLogMapper;

    public void save(ActionLogEntity entity) {
        actionLogMapper.toDto(actionLogRepository.save(entity));
    }

    public List<ActionLogDto> findAll() {
        try (Stream<ActionLogEntity> stream = actionLogRepository.findAll().stream()) {
            return stream
                    .map(actionLogMapper::toDto)
                    .toList();
        }
    }
}
