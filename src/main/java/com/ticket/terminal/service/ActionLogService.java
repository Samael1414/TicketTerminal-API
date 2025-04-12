package com.ticket.terminal.service;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.mapper.ActionLogMapper;
import com.ticket.terminal.repository.ActionLogRepository;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final UserRepository userRepository;
    private final ActionLogMapper actionLogMapper;

    public ActionLogDto save(ActionLogDto dto) {
        UsersEntity user = userRepository.findById(Long.valueOf(dto.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ActionLogEntity entity = new ActionLogEntity();
        entity.setUser(user);
        entity.setActionType(dto.getActionType());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        entity.setActorName(dto.getActorName());

        return actionLogMapper.toDto(actionLogRepository.save(entity));
    }

    public List<ActionLogDto> findAll() {
        return actionLogRepository.findAll()
                .stream()
                .map(actionLogMapper::toDto)
                .toList();
    }
}
