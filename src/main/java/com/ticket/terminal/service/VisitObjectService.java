package com.ticket.terminal.service;

import com.ticket.terminal.dto.VisitObjectCreateDto;
import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.VisitObjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitObjectService {

    // Репозиторий для работы с таблицей visit_objects
    private final VisitObjectRepository visitObjectRepository;

    // Маппер для преобразования между Entity и DTO
    private final VisitObjectMapper visitObjectMapper;

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
        VisitObjectEntity entity = new VisitObjectEntity();

        // Устанавливаем поля из DTO в entity
        entity.setVisitObjectName(dto.getVisitObjectName());        // Название объекта посещения (например, "Домик Пушкина")
        entity.setCategoryVisitorId(dto.getCategoryVisitorId());    // Привязка к категории посетителя (если есть, может быть null)
        entity.setAddress(dto.getAddress());                        // Адрес или местоположение объекта (текстовое описание)
        entity.setComment(dto.getComment());                        // Дополнительный комментарий/примечание к объекту
        entity.setIsRequire(dto.getIsRequire());                    // Обязателен ли объект для посещения (true — нельзя пропустить)


        // Сохраняем и возвращаем как DTO
        return visitObjectMapper.toDto(visitObjectRepository.save(entity));
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
    }
}
