package com.ticket.terminal.service;

import com.ticket.terminal.dto.ActionLogDto;
import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
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

        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "CREATE_USER",
                  String.format("Cоздан пользователь: %s", saved.getUserName()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        return usersMapper.toDto(saved);
    }

    public List<UsersResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(usersMapper::toDto)
                .toList();
    }

    public UsersResponseDto findById(Long id) {
        return userRepository.findById(id)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

    public void deleteById(Long id) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.deleteById(id);

        UsersEntity currentUser = getCurrentUser();

        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "DELETE_USER",
                String.format("Удален пользователь: %s", user.getUserName()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));
    }

    public UsersResponseDto update(Long id, UsersCreateDto dto) {
        UsersEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        entity.setUserName(dto.getUserName());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setCreatedAt(entity.getCreatedAt());

        UsersEntity updated = userRepository.save(entity);
        UsersEntity currentUser = getCurrentUser();

        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "UPDATE_USER",
                String.format("Обновлён пользователь: %s", updated.getUserName()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        return usersMapper.toDto(updated);
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
    }

}
