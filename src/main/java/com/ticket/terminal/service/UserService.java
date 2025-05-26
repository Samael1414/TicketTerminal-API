package com.ticket.terminal.service;

import com.ticket.terminal.dto.UsersCreateDto;
import com.ticket.terminal.dto.UsersResponseDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UserPermissionEntity;
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

/**
 * Сервис для управления пользователями системы
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;

    /**
     * Создание нового пользователя с правами доступа
     * 
     * @param dto DTO с данными пользователя
     * @return DTO с данными созданного пользователя
     */
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
        
        // Создаем пользователя
        UsersEntity entity = usersMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsRoot(dto.getIsRoot() != null && dto.getIsRoot());
        
        // Проверяем и настраиваем права доступа
        setupUserPermissions(entity, dto);
        UsersEntity saved = userRepository.save(entity);

        try {
            UsersEntity currentUser = getCurrentUser();
            ActionLogEntity logEntity = new ActionLogEntity();
            logEntity.setUser(currentUser);
            logEntity.setActionType("CREATE_USER");
            logEntity.setDescription(String.format("Создан пользователь: %s", saved.getUserName()));
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

    /**
     * Удаление пользователя по ID
     * 
     * @param id ID пользователя
     * @throws EntityNotFoundException если пользователь не найден
     * @throws IllegalStateException если пытаемся удалить root-пользователя
     */
    public void deleteById(Long id) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
                
        // Проверяем, не пытаемся ли мы удалить root-пользователя
        if (user.getIsRoot() != null && user.getIsRoot()) {
            throw new IllegalStateException("Root-пользователь не может быть удален");
        }
        
        // Проверяем, есть ли права на удаление пользователей
        UsersEntity currentUser = getCurrentUser();
        if (currentUser.getPermissions() == null || !currentUser.getPermissions().getCanManageUsers()) {
            // Если пользователь root, то у него есть все права
            if (currentUser.getIsRoot() == null || !currentUser.getIsRoot()) {
                throw new IllegalStateException("Недостаточно прав для удаления пользователей");
            }
        }
        
        userRepository.deleteById(id);

        try {
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

    /**
     * Обновление данных пользователя и его прав доступа
     * 
     * @param id ID пользователя
     * @param dto DTO с новыми данными пользователя
     * @return DTO с обновленными данными пользователя
     */
    public UsersResponseDto update(Long id, UsersCreateDto dto) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
                
        // Проверяем, не пытаемся ли мы изменить root-пользователя
        if (user.getIsRoot() != null && user.getIsRoot()) {
            UsersEntity currentUser = getCurrentUser();
            // Только сам root-пользователь может изменять себя
            if (currentUser.getId() != user.getId()) {
                throw new IllegalStateException("Root-пользователь не может быть изменен другими пользователями");
            }
        }

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

        // Обновляем пароль только если он предоставлен и не пуст
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // Обновляем признак root только если текущий пользователь - root
        UsersEntity currentUser = getCurrentUser();
        if (currentUser.getIsRoot() != null && currentUser.getIsRoot()) {
            user.setIsRoot(dto.getIsRoot() != null && dto.getIsRoot());
        }
        
        // Обновляем права доступа
        if (dto.getPermissions() != null) {
            // Если у пользователя еще нет прав доступа, создаем их
            if (user.getPermissions() == null) {
                UserPermissionEntity permissions = new UserPermissionEntity();
                permissions.setUser(user);
                user.setPermissions(permissions);
            }
            
            // Обновляем права доступа
            UserPermissionEntity permissions = user.getPermissions();
            permissions.setCanManageUsers(dto.getPermissions().getCanManageUsers() != null && dto.getPermissions().getCanManageUsers());
            permissions.setCanManageServices(dto.getPermissions().getCanManageServices() != null && dto.getPermissions().getCanManageServices());
            permissions.setCanManageCategories(dto.getPermissions().getCanManageCategories() != null && dto.getPermissions().getCanManageCategories());
            permissions.setCanManageVisitObjects(dto.getPermissions().getCanManageVisitObjects() != null && dto.getPermissions().getCanManageVisitObjects());
            permissions.setCanViewReports(dto.getPermissions().getCanViewReports() != null && dto.getPermissions().getCanViewReports());
            permissions.setCanManageSettings(dto.getPermissions().getCanManageSettings() != null && dto.getPermissions().getCanManageSettings());
            permissions.setCanManageOrders(dto.getPermissions().getCanManageOrders() != null && dto.getPermissions().getCanManageOrders());
            permissions.setCanExportData(dto.getPermissions().getCanExportData() != null && dto.getPermissions().getCanExportData());
            permissions.setCanImportData(dto.getPermissions().getCanImportData() != null && dto.getPermissions().getCanImportData());
            
            // Если пользователь root, то все права доступа включены
            if (user.getIsRoot() != null && user.getIsRoot()) {
                permissions.setCanManageUsers(true);
                permissions.setCanManageServices(true);
                permissions.setCanManageCategories(true);
                permissions.setCanManageVisitObjects(true);
                permissions.setCanViewReports(true);
                permissions.setCanManageSettings(true);
                permissions.setCanManageOrders(true);
                permissions.setCanExportData(true);
                permissions.setCanImportData(true);
            }
        }
        
        UsersEntity updated = userRepository.save(user);

        try {
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
        // Создаем и настраиваем права доступа
        UserPermissionEntity permissions = new UserPermissionEntity();
        permissions.setUser(entity);
        
        // Если пользователь root, то все права доступа включены
        if (entity.getIsRoot()) {
            permissions.setCanManageUsers(true);
            permissions.setCanManageServices(true);
            permissions.setCanManageCategories(true);
            permissions.setCanManageVisitObjects(true);
            permissions.setCanViewReports(true);
            permissions.setCanManageSettings(true);
            permissions.setCanManageOrders(true);
            permissions.setCanExportData(true);
            permissions.setCanImportData(true);
        } else if (dto.getPermissions() != null) {
            // Иначе устанавливаем права из DTO
            permissions.setCanManageUsers(dto.getPermissions().getCanManageUsers() != null && dto.getPermissions().getCanManageUsers());
            permissions.setCanManageServices(dto.getPermissions().getCanManageServices() != null && dto.getPermissions().getCanManageServices());
            permissions.setCanManageCategories(dto.getPermissions().getCanManageCategories() != null && dto.getPermissions().getCanManageCategories());
            permissions.setCanManageVisitObjects(dto.getPermissions().getCanManageVisitObjects() != null && dto.getPermissions().getCanManageVisitObjects());
            permissions.setCanViewReports(dto.getPermissions().getCanViewReports() != null && dto.getPermissions().getCanViewReports());
            permissions.setCanManageSettings(dto.getPermissions().getCanManageSettings() != null && dto.getPermissions().getCanManageSettings());
            permissions.setCanManageOrders(dto.getPermissions().getCanManageOrders() != null && dto.getPermissions().getCanManageOrders());
            permissions.setCanExportData(dto.getPermissions().getCanExportData() != null && dto.getPermissions().getCanExportData());
            permissions.setCanImportData(dto.getPermissions().getCanImportData() != null && dto.getPermissions().getCanImportData());
        }
        
        entity.setPermissions(permissions);
    }
}
