package com.ticket.terminal.service;

import com.ticket.terminal.dto.user.UserPermissionDto;
import com.ticket.terminal.dto.user.UsersCreateDto;
import com.ticket.terminal.dto.user.UsersResponseDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UserPermissionEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.mapper.UserPermissionMapper;
import com.ticket.terminal.mapper.UsersMapper;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления пользователями системы
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;
    private final UserPermissionMapper userPermissionMapper;

    /**
     * Создание нового пользователя с правами доступа
     * 
     * @param dto DTO с данными пользователя
     * @return DTO с данными созданного пользователя
     */
    @Transactional
    public UsersResponseDto createUser(UsersCreateDto dto) {
        // — проверки уникальности
        validateEmailUniqueness(dto.getEmail());
        validateUserNameUniqueness(dto.getUserName());
        // — собираем сущность
        UsersEntity entity = buildUserEntity(dto);
        // — настраиваем права
        setupUserPermissions(entity, dto);
        // — сохраняем
        UsersEntity saved = userRepository.save(entity);
        // — логируем факт создания
        logAction(
                "CREATE_USER",
                "Создан пользователь: " + saved.getUserName()
        );

        // — возвращаем DTO
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

    /**
     * Удаление пользователя по ID
     * 
     * @param id ID пользователя
     * @throws EntityNotFoundException если пользователь не найден
     * @throws IllegalStateException если пытаемся удалить root-пользователя
     */
    @Transactional
    public void deleteById(Long id) {
        UsersEntity target = findUserById(id);
        forbidDeletingRoot(target);

        UsersEntity current = getCurrentUser();
        checkDeletePermission(current);

        userRepository.deleteById(id);
        logAction("DELETE_USER",
                "Удалён пользователь: " + target.getUserName()
        );
    }

    /**
     * Обновление данных пользователя и его прав доступа
     * 
     * @param id ID пользователя
     * @param dto DTO с новыми данными пользователя
     * @return DTO с обновленными данными пользователя
     */
    @Transactional
    public UsersResponseDto update(Long id, UsersCreateDto dto) {
        // 1) Загружаем и проверяем существование
        UsersEntity user = findUserById(id);

        // 2) Блокируем изменения чужого root
        forbidEditingOtherRoot(user);

        // 3) Проверяем уникальность (только если поля поменялись)
        validateEmailChange(id, dto.getEmail(), user.getEmail());
        validateUserNameChange(id, dto.getUserName(), user.getUserName());

        // 4) Обновляем базовые поля
        updateBasicInfo(user, dto);

        // 5) Пароль
        updatePassword(user, dto.getPassword());

        // 6) Флаг root можно менять только самому root
        updateRootFlag(user, dto.getIsRoot());

        // 7) Права доступа
        updatePermissions(user, dto.getPermissions());

        // 8) Сохраняем изменения
        UsersEntity updated = userRepository.save(user);

        // 9) Логируем факт изменения
        logAction("UPDATE_USER", "Обновлён пользователь: " + updated.getUserName());

        return usersMapper.toDto(updated);
    }

    /**
     * Получает текущего пользователя
     * 
     * @return Сущность текущего пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
    
    /**
     * Получает информацию о текущем пользователе
     * 
     * @return DTO с информацией о текущем пользователе
     */
    public UsersResponseDto getCurrentUserInfo() {
        return usersMapper.toDto(getCurrentUser());
    }
    
    /**
     * Проверяет, является ли текущий пользователь root-пользователем
     * 
     * @return true, если текущий пользователь является root-пользователем
     */
    public boolean isCurrentUserRoot() {
        UsersEntity user = getCurrentUser();
        return user.getIsRoot() != null && user.getIsRoot();
    }
    
    /**
     * Настраивает права доступа пользователя на основе DTO
     *
     * @param entity Сущность пользователя
     * @param dto DTO с данными пользователя
     */
    private void setupUserPermissions(UsersEntity entity, UsersCreateDto dto) {
        // Создаём новую сущность прав
        UserPermissionEntity permission = new UserPermissionEntity();
        permission.setUser(entity);

        // Если у DTO задан блок permissions — мапим поля одним вызовом
        if (dto.getPermissions() != null) {
            userPermissionMapper.updateEntityFromDto(dto.getPermissions(), permission);
        }

        // Если это root — «включаем всё»
        if (Boolean.TRUE.equals(entity.getIsRoot())) {
            permission.enableAll();
        }

        entity.setPermissions(permission);
    }


    private void logAction(String actionType, String description) {
        try {
            UsersEntity current = getCurrentUser();
            actionLogService.save(
                    ActionLogEntity.builder()
                            .user(current)
                            .actionType(actionType)
                            .description(description)
                            .createdAt(LocalDateTime.now())
                            .actorName(current.getUserName())
                            .build()
            );
        } catch (Exception e) {
            // Нефатальное логирование
            System.err.println(
                    "Ошибка логирования действия " + actionType + ": " + e.getMessage()
            );
        }
    }

    private UsersEntity buildUserEntity(UsersCreateDto dto) {
        UsersEntity entity = usersMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsRoot(Boolean.TRUE.equals(dto.getIsRoot()));
        return entity;
    }

    private void validateUserNameUniqueness(String userName) {
        if (userRepository
                .findByUserNameIgnoreCase(userName)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Пользователь с таким именем уже существует"
            );
        }
    }

    private void validateEmailUniqueness(String email) {
        if (StringUtils.hasText(email)
                && userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Пользователь с таким email уже существует"
            );
        }
    }

    private UsersEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Пользователь не найден: " + id)
                );
    }

    private void forbidDeletingRoot(UsersEntity user) {
        if (Boolean.TRUE.equals(user.getIsRoot())) {
            throw new IllegalStateException("Root-пользователь не может быть удалён");
        }
    }

    private void checkDeletePermission(UsersEntity current) {
        boolean canManage = current.getPermissions() != null
                && current.getPermissions().getCanManageUsers();
        boolean isRoot = Boolean.TRUE.equals(current.getIsRoot());
        if (!canManage && !isRoot) {
            throw new IllegalStateException("Недостаточно прав для удаления пользователей");
        }
    }

    private void updatePermissions(UsersEntity user, UserPermissionDto dto) {
        // если нет dto и мы не root — просто выходим
        if (dto == null && !Boolean.TRUE.equals(user.getIsRoot())) {
            return;
        }
        // получаем или создаём объект прав
        UserPermissionEntity permission = Optional.ofNullable(user.getPermissions())
                .orElseGet(() -> {
                    var entity = userPermissionMapper.toEntity(dto != null ? dto : new UserPermissionDto());
                    entity.setUser(user);
                    user.setPermissions(entity);
                    return entity;
                });

        if (Boolean.TRUE.equals(user.getIsRoot())) {
            // для root’а включаем все
            permission.enableAll();      // <— добавьте в сущность метод, который выставит все boolean = true
        } else {
            // мапим только непустые поля из dto
            userPermissionMapper.updateEntityFromDto(dto, permission);
        }
    }

    private void updateBasicInfo(UsersEntity user, UsersCreateDto dto) {
        user.setUserName(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
    }

    private void updatePassword(UsersEntity user, String rawPassword) {
        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
    }

    private void updateRootFlag(UsersEntity user, Boolean newRootFlag) {
        UsersEntity users = getCurrentUser();
        if (Boolean.TRUE.equals(users.getIsRoot())) {
            user.setIsRoot(Boolean.TRUE.equals(newRootFlag));
        }
    }

    private void forbidEditingOtherRoot(UsersEntity user) {
        if (Boolean.TRUE.equals(user.getIsRoot())) {
            UsersEntity me = getCurrentUser();
            if (!me.getId().equals(user.getId())) {
                throw new IllegalStateException("Root-пользователь не может быть изменён другим пользователем");
            }
        }
    }

    private void validateEmailChange(Long id, String newEmail, String oldEmail) {
        if (newEmail == null || newEmail.equals(oldEmail)) {
            return;
        }
        userRepository.findByEmail(newEmail)
                .filter(usersEntity -> !usersEntity.getId().equals(id))
                .ifPresent(usersEntity -> {
                    throw new IllegalArgumentException("Пользователь с таким email уже существует");
                });
    }


    private void validateUserNameChange(Long id, String newName, String oldName) {
        if (newName == null || newName.equalsIgnoreCase(oldName)) {
            return;
        }
        userRepository.findByUserNameIgnoreCase(newName)
                .filter(usersEntity -> !usersEntity.getId().equals(id))
                .ifPresent(usersEntity -> {
                    throw new IllegalArgumentException("Пользователь с таким именем уже существует");
                });
    }




}
