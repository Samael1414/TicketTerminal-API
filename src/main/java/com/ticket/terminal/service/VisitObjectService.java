package com.ticket.terminal.service;

import com.ticket.terminal.dto.visit.VisitObjectCreateDto;
import com.ticket.terminal.dto.visit.VisitObjectDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.exception.EntityNotFoundException;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.UserRepository;
import com.ticket.terminal.repository.VisitObjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitObjectService {

    private final VisitObjectRepository visitObjectRepository;
    private final VisitObjectMapper visitObjectMapper;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;

    public List<VisitObjectDto> getAllVisitObjects() {
        return visitObjectMapper.toDtoList(
                visitObjectRepository.findAll()
        );
    }

    @Transactional
    public VisitObjectDto createVisitObject(VisitObjectCreateDto dto) {
        VisitObjectEntity entity = visitObjectMapper.toEntity(dto);
        VisitObjectEntity saved = visitObjectRepository.save(entity);
        logAction(
                "CREATE_VISIT_OBJECT",
                "Создан объект посещения: " + saved.getVisitObjectName()
        );

        return visitObjectMapper.toDto(saved);
    }

    @Transactional
    public VisitObjectDto updateVisitObject(Long id, VisitObjectCreateDto dto) {
        VisitObjectEntity existing = findByIdOrThrow(id);

        visitObjectMapper.updateEntityFromDto(dto, existing);

        VisitObjectEntity updated = visitObjectRepository.save(existing);

        logAction(
                "UPDATE_VISIT_OBJECT",
                "Обновлён объект посещения: " + updated.getVisitObjectName()
        );

        return visitObjectMapper.toDto(updated);
    }

    @Transactional
    public void deleteVisitObject(Long id) {
        findByIdOrThrow(id);
        visitObjectRepository.deleteById(id);

        logAction(
                "DELETE_VISIT_OBJECT",
                "Удалён объект посещения: id=" + id
        );
    }

    private VisitObjectEntity findByIdOrThrow(Long id) {
        return visitObjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "VisitObject не найден: id=" + id
                ));
    }

    private void logAction(String actionType, String description) {
        UsersEntity user = getCurrentUser();
        actionLogService.save(
                ActionLogEntity.builder()
                        .user(user)
                        .actionType(actionType)
                        .description(description)
                        .createdAt(LocalDateTime.now())
                        .actorName(user.getUserName())
                        .build()
        );
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        return userRepository
                .findByUserNameIgnoreCase(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Текущий пользователь не найден"
                ));
    }
}
