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
        UsersEntity entity = usersMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setCreatedAt(LocalDateTime.now());
        UsersEntity saved = userRepository.save(entity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_USER")
                .description(String.format("Cоздан пользователь: %s", saved.getUserName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());


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

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("DELETE_USER")
                .description(String.format("Удален пользователь: %s", user.getUserName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());
    }

    public UsersResponseDto update(Long id, UsersCreateDto dto) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        user.setUserName(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(user.getCreatedAt());
        UsersEntity updated = userRepository.save(user);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("UPDATE_USER")
                .description(String.format("Обновлён пользователь: %s", updated.getUserName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return usersMapper.toDto(updated);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }

}
