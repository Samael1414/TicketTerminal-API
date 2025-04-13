package com.ticket.terminal.service;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.mapper.ActionLogMapper;
import com.ticket.terminal.repository.ActionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final ActionLogMapper actionLogMapper;

    public ActionLogDto save(ActionLogEntity entity) {
        return actionLogMapper.toDto(actionLogRepository.save(entity));
    }

    public List<ActionLogDto> findAll() {
        return actionLogRepository.findAll()
                .stream()
                .map(actionLogMapper::toDto)
                .toList();
    }
}
