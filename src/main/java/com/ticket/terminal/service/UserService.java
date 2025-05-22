package com.ticket.terminal.service;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.mapper.UsersMapper;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;

    public UsersResponseDto createUser(UsersCreateDto dto) {
        // Проверяем уникальность email
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
        }
        
        // Проверяем уникальность userName
        if (userRepository.findByUserNameIgnoreCase(dto.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        
        UsersEntity entity = usersMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setCreatedAt(LocalDateTime.now());
        UsersEntity saved = userRepository.save(entity);

        try {
            UsersEntity currentUser = getCurrentUser();
            ActionLogEntity logEntity = new ActionLogEntity();
            logEntity.setUser(currentUser);
            logEntity.setActionType("CREATE_USER");
            logEntity.setDescription(String.format("Cоздан пользователь: %s", saved.getUserName()));
            logEntity.setCreatedAt(LocalDateTime.now());
            logEntity.setActorName(currentUser.getUserName());
            actionLogService.save(logEntity);
        } catch (Exception e) {
            // Логируем ошибку, но не прерываем основной процесс
            System.err.println("Ошибка при создании записи в журнале действий: " + e.getMessage());
        }

        return usersMapper.toDto(saved);
    }

    public List<UsersResponseDto> findAll() {
        try (Stream<UsersEntity> stream = userRepository.findAll().stream()) {
            return stream.map(usersMapper::toDto).toList();
        }
    }

    public UsersResponseDto findById(Long id) {
        return userRepository.findById(id)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    }

    public void deleteById(Long id) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        userRepository.deleteById(id);

        try {
            UsersEntity currentUser = getCurrentUser();
            ActionLogEntity logEntity = new ActionLogEntity();
            logEntity.setUser(currentUser);
            logEntity.setActionType("DELETE_USER");
            logEntity.setDescription(String.format("Удален пользователь: %s", user.getUserName()));
            logEntity.setCreatedAt(LocalDateTime.now());
            logEntity.setActorName(currentUser.getUserName());
            actionLogService.save(logEntity);
        } catch (Exception e) {
            // Логируем ошибку, но не прерываем основной процесс
            System.err.println("Ошибка при создании записи в журнале действий: " + e.getMessage());
        }
    }

    public UsersResponseDto update(Long id, UsersCreateDto dto) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Проверяем уникальность email, если он изменился
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (!userRepository.findByEmailAndIdNot(dto.getEmail(), id).isEmpty()) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
        }

        // Проверяем уникальность userName, если он изменился
        if (dto.getUserName() != null && !dto.getUserName().equals(user.getUserName())) {
            if (!userRepository.findByUserNameAndIdNot(dto.getUserName(), id).isEmpty()) {
                throw new IllegalArgumentException("Пользователь с таким именем уже существует");
            }
        }

        user.setUserName(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(user.getCreatedAt());
        UsersEntity updated = userRepository.save(user);

        try {
            UsersEntity currentUser = getCurrentUser();
            ActionLogEntity logEntity = new ActionLogEntity();
            logEntity.setUser(currentUser);
            logEntity.setActionType("UPDATE_USER");
            logEntity.setDescription(String.format("Обновлён пользователь: %s", updated.getUserName()));
            logEntity.setCreatedAt(LocalDateTime.now());
            logEntity.setActorName(currentUser.getUserName());
            actionLogService.save(logEntity);
        } catch (Exception e) {
            // Логируем ошибку, но не прерываем основной процесс
            System.err.println("Ошибка при создании записи в журнале действий: " + e.getMessage());
        }

        return usersMapper.toDto(updated);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }

}
