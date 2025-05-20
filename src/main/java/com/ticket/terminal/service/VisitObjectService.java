package com.ticket.terminal.service;

import com.ticket.terminal.dto.VisitObjectCreateDto;
import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.entity.ActionLogEntity;
import com.ticket.terminal.entity.UsersEntity;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.UserRepository;
import com.ticket.terminal.repository.VisitObjectRepository;
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
public class VisitObjectService {

    // Репозиторий для работы с таблицей visit_objects
    private final VisitObjectRepository visitObjectRepository;

    // Маппер для преобразования между Entity и DTO
    private final VisitObjectMapper visitObjectMapper;
    private final ActionLogService actionLogService;
    private UserRepository userRepository;

    /**
     * Получить список всех объектов посещения из базы
     * и преобразовать в DTO список для передачи на фронт
     *
     * @return список объектов VisitObjectDto
     */
    public List<VisitObjectDto> getAllVisitObjects() {
        return visitObjectMapper.toDtoList(visitObjectRepository.findAll());
    }

    /**
     * Создает новый объект посещения.
     * Преобразует входной DTO в entity, сохраняет в базу,
     * и возвращает обратно DTO с данными, включая сгенерированный ID.
     *
     * @param dto DTO с данными нового объекта
     * @return созданный VisitObjectDto
     */
    @Transactional
    public VisitObjectDto createVisitObject(VisitObjectCreateDto dto) {
        // Создаем временный DTO с теми же полями, что и CreateDto
        VisitObjectDto tempDto = VisitObjectDto.builder()
                .visitObjectName(dto.getVisitObjectName())
                .categoryVisitorId(dto.getCategoryVisitorId())
                .address(dto.getAddress())
                .comment(dto.getComment())
                .isRequire(dto.getIsRequire())
                .groupVisitObjectId(dto.getGroupVisitObjectId())
                .build();

        // Используем маппер для создания сущности (игнорирует ID)
        VisitObjectEntity entity = visitObjectMapper.toEntity(tempDto);
        VisitObjectEntity saved = visitObjectRepository.save(entity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_VISIT_OBJECT")
                .description(String.format("Создан объект посещения: %s", saved.getVisitObjectName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());


        // Сохраняем и возвращаем как DTO
        return visitObjectMapper.toDto(saved);
    }

    /**
     * Обновление существующего объекта посещения по ID.
     *
     * Если объект не найден — выбрасывает исключение.
     * Иначе обновляет все его поля на основе DTO.
     *
     * @param id   ID обновляемого объекта
     * @param dto  новые значения полей
     * @return обновлённый объект в виде DTO
     */
    @Transactional
    public VisitObjectDto updateVisitObject(Long id, VisitObjectCreateDto dto) {
        // Найти существующий объект или выбросить исключение
        VisitObjectEntity entity = visitObjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VisitObject не найден: id=" + id));

        // Обновить поля из DTO
        entity.setVisitObjectName(dto.getVisitObjectName());        // Название объекта (обновлённое)
        entity.setCategoryVisitorId(dto.getCategoryVisitorId());    // ID категории посетителя
        entity.setAddress(dto.getAddress());                        // Адрес
        entity.setComment(dto.getComment());                        // Комментарий
        entity.setIsRequire(dto.getIsRequire());                    // Признак обязательности
        // groupVisitObjectId маппится из id в маппере VisitObjectMapper

        // Сохранить и вернуть как DTO
        VisitObjectEntity updated = visitObjectRepository.save(entity);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("UPDATE_VISIT_OBJECT")
                .description(String.format("Обновлён объект посещения: %s", updated.getVisitObjectName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());

        return visitObjectMapper.toDto(updated);
    }

    /**
     * Удаляет объект посещения по ID.
     * Предполагается, что связанные записи (например, в price)
     * обрабатываются через ON DELETE CASCADE или отдельно в бизнес-логике.
     *
     * @param id идентификатор удаляемого объекта
     */
    @Transactional
    public void deleteVisitObject(Long id) {
        visitObjectRepository.deleteById(id);

        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("DELETE_VISIT_OBJECT")
                .description(String.format("Удалён объект посещения: id=%d", id))
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
