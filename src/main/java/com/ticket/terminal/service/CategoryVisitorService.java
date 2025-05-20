package com.ticket.terminal.service;

import com.ticket.terminal.dto.CategoryVisitorCreateDto;
import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.CategoryVisitorEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.mapper.CategoryVisitorMapper;
import com.ticket.terminal.repository.CategoryVisitorRepository;
import com.ticket.terminal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryVisitorService {

    // Репозиторий для работы с сущностью CategoryVisitorEntity
    private final CategoryVisitorRepository categoryVisitorRepository;

    // Маппер для преобразования между Entity и DTO
    private final CategoryVisitorMapper categoryVisitorMapper;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    /**
     * Получение всех категорий посетителей
     *
     * Используется для отображения списка доступных категорий (взрослые, дети и т.д.).
     * Возвращает DTO со всеми полями, включая ID.
     */
    public List<CategoryVisitorDto> getAll() {
        return categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());
    }

    /**
     * Создание новой категории посетителя
     *
     * Преобразует DTO в сущность, сохраняет в БД, затем возвращает сохранённый объект как DTO.
     * Используется при добавлении новой категории через админку или API.
     *
     * @param dto входящие данные с названием, группой и т.д.
     * @return сохранённая категория
     */
    @Transactional
    public CategoryVisitorDto create(CategoryVisitorCreateDto dto) {
        // Преобразуем DTO в Entity
        CategoryVisitorEntity entity = categoryVisitorMapper.toEntity(dto);

        // Сохраняем в БД
        CategoryVisitorEntity saved = categoryVisitorRepository.save(entity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_CATEGORY_VISITOR")
                .description(String.format("Создана категория посетителя: %s", saved.getCategoryVisitorName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        // Возвращаем обратно как DTO
        return categoryVisitorMapper.toDto(saved);
    }

    /**
     * Обновление существующей категории.
     *
     * Проверяет наличие по ID, обновляет поля, сохраняет и возвращает результат.
     *
     * @param id идентификатор обновляемой категории
     * @param dto новые значения для обновления
     * @return обновлённая категория
     */
    @Transactional
    public CategoryVisitorDto update(Long id, CategoryVisitorCreateDto dto) {
        CategoryVisitorEntity entity = categoryVisitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена с ID = " + id));

        entity.setCategoryVisitorName(dto.getCategoryVisitorName());

        CategoryVisitorEntity updated = categoryVisitorRepository.save(entity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("UPDATE_CATEGORY_VISITOR")
                .description(String.format("Обновлена категория посетителя: %s", updated.getCategoryVisitorName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return categoryVisitorMapper.toDto(updated);
    }

    /**
     * Удаление категории по ID
     *
     * Удаляет категорию из БД, если она не используется в связанной таблице (например, prices).
     * Если включён каскад, связанные цены также удалятся.
     *
     * @param id ID категории для удаления
     */
    @Transactional
    public void delete(Long id) {
        categoryVisitorRepository.deleteById(id);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("DELETE_CATEGORY_VISITOR")
                .description(String.format("Удалена категория посетителя: id=%d", id))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());
    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userRepository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
