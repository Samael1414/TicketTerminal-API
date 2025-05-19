package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.mapper.*;
import com.ticket.terminal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ServiceService {


    private final ServiceRepository serviceRepository;
    private final SeanceGridRepository seanceGridRepository;
    private final ServiceMapper serviceMapper;
    private final SeanceGridMapper seanceGridMapper;
    private final EditableServiceMapper editableServiceMapper;
    private final VisitObjectMapper visitObjectMapper;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorMapper categoryVisitorMapper;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final PriceMapper priceMapper;
    private final PriceRepository priceRepository;

    public SimpleServiceResponseDto getSimpleService() {
        List<SeanceGridDto> allSeanceGrid;
        try (Stream<SeanceGridEntity> stream = seanceGridRepository.findAll().stream()){
            allSeanceGrid = stream
                    .map(seanceGridMapper::toDto)
                    .toList();
        }
        List<SimpleServiceDto> service;
        try (Stream<ServiceEntity> stream = serviceRepository.findAllSimpleServices().stream()) {
            service = stream
                    .map(entity -> {
                        SimpleServiceDto dto = serviceMapper.toSimpleDto(entity);
                        dto.setSeanceGrid(allSeanceGrid);
                        return dto;
                    }).toList();
        }

        return SimpleServiceResponseDto.builder().service(service).seanceGrid(allSeanceGrid).build();
    }

    public EditableServiceResponseDto getEditableServices() {
        List<SeanceGridDto> allSeanceGrid = seanceGridMapper.toDtoList(seanceGridRepository.findAll());
        List<VisitObjectItemDto> allVisitObjects = visitObjectRepository.findAll().stream()
                .map(object -> VisitObjectItemDto.builder()
                        .visitObjectId(object.getId())
                        .visitObjectName(object.getVisitObjectName())
                        .categoryVisitorId(object.getCategoryVisitorId())
                        .address(object.getAddress())
                        .comment(object.getComment())
                        .build())
                .toList();

        List<CategoryVisitorDto> allCategories = categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());


        List<EditableServiceDto> services;
        try (Stream<ServiceEntity> stream = serviceRepository.findAllEditableServices().stream()) {
            services = stream.map(service -> {
                EditableServiceDto dto = editableServiceMapper.toDto(service);

                List<VisitObjectEntity> relatedVisitObjects = visitObjectRepository.findAllByServiceId(service.getId());
                List<VisitObjectDto> objectsForService = relatedVisitObjects.stream()
                        .map(object -> VisitObjectDto.builder()
                                .visitObjectId(object.getId())
                                .visitObjectName(object.getVisitObjectName())
                                .groupVisitObjectId(object.getGroupVisitObjectId())
                                .isRequire(Boolean.TRUE)
                                .build())
                        .toList();
                dto.setVisitObjects(objectsForService);

                List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));
                dto.setPrices(prices);

                Set<Long> categoryIdsInPrice = prices.stream()
                        .map(PriceDto::getCategoryVisitorId)
                        .collect(Collectors.toSet());

                List<CategoryVisitorEntity> categoryEntities = categoryVisitorRepository.findAllById(categoryIdsInPrice);
                List<CategoryVisitorDto> categoriesForService = categoryEntities.stream()
                        .map(visitor -> CategoryVisitorDto.builder()
                                .categoryVisitorId(visitor.getId())
                                .categoryVisitorName(visitor.getCategoryVisitorName())
                                .groupCategoryVisitorId(visitor.getGroupCategoryVisitorId())
                                .requireVisitorCount(0)
                                .build())
                        .toList();
                dto.setCategoryVisitor(categoriesForService);

                dto.setSeanceGrid(allSeanceGrid);
                return dto;
            }).toList();
        }

        return EditableServiceResponseDto.builder()
                .visitObjects(allVisitObjects) // 🔥 добавь это
                .categoryVisitor(allCategories) // 🔥 и это
                .seanceGrid(allSeanceGrid)
                .service(services)
                .build();
    }



    /**
     * Создание новой услуги на основе данных DTO.
     * Привязывает существующие объекты посещения (visitObjects),
     * создаёт сущности цены (price) и возвращает расширенное представление услуги.
     */
    @Transactional
    public EditableServiceDto createService(ServiceCreateDto dto) {
        // Создаём новую сущность Service из DTO
        ServiceEntity entity = new ServiceEntity();
        applyDtoToEntity(dto, entity);

        // Сохраняем в БД, чтобы получить сгенерированный ID
        serviceRepository.save(entity);

        // Привязка существующих VisitObject к новой услуге
        // Важно: мы не создаём новые объекты, а связываем уже существующие (по ID)
        if (dto.getVisitObject() != null) {
            for (VisitObjectDto visitObjectDto : dto.getVisitObject()) {
                Long visitObjectId = visitObjectDto.getVisitObjectId();
                if (visitObjectId != null) {
                    // Проверка существования объекта посещения
                    VisitObjectEntity visitObject = visitObjectRepository.findById(visitObjectId)
                            .orElseThrow(() -> new RuntimeException("VisitObject не найден: " + visitObjectId));

                    // Привязка к текущей услуге
                    visitObject.setService(entity);
                    visitObjectRepository.save(visitObject);
                }
            }
        }

        // Создание записей цен (price)
        if (dto.getPrice() != null) {
            for (PriceDto priceDto : dto.getPrice()) {
                PriceEntity priceEntity = new PriceEntity();

                // Привязка к текущей услуге
                priceEntity.setService(entity);

                // Привязка к объекту посещения (если указан)
                if (priceDto.getVisitObjectId() != null) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(priceDto.getVisitObjectId())
                            .orElseThrow(() -> new RuntimeException("VisitObject не найден: " + priceDto.getVisitObjectId()));
                    priceEntity.setVisitObject(visitObject);
                }

                // Привязка к категории посетителей (если указана)
                if (priceDto.getCategoryVisitorId() != null) {
                    CategoryVisitorEntity category = categoryVisitorRepository.findById(priceDto.getCategoryVisitorId())
                            .orElseThrow(() -> new RuntimeException("CategoryVisitor не найдена: " + priceDto.getCategoryVisitorId()));
                    priceEntity.setCategoryVisitor(category);
                }

                // Установка стоимости
                priceEntity.setCost(priceDto.getCost());

                // Сохранение в БД
                priceRepository.save(priceEntity);
            }
        }

        // Возврат DTO расширенного формата
        return buildEditableServiceDto(entity);
    }

    /**
     * Обновление существующей услуги по ID.
     * Перезаписывает сущность, заново связывает объекты посещения и цены.
     */
    @Transactional
    public EditableServiceDto updateService(Long id, ServiceUpdateDto dto) {
        // Загрузка существующей сущности по ID
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service не найден"));

        // Обновление полей сущности из DTO
        applyDtoToEntity(dto, entity);
        serviceRepository.save(entity);

        // Удаляем все старые цены, связанные с услугой
        // Не трогаем сами visit_objects, они являются справочными
        priceRepository.deleteAllByServiceId(id);

        // Повторная привязка указанных visit_objects к услуге
        if (dto.getVisitObject() != null) {
            for (VisitObjectDto visitObjectDto : dto.getVisitObject()) {
                Long visitObjectId = visitObjectDto.getVisitObjectId();
                if (visitObjectId != null) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(visitObjectId)
                            .orElseThrow(() -> new RuntimeException("VisitObject не найден: " + visitObjectId));

                    // Обновление связи с текущей услугой
                    visitObject.setService(entity);
                    visitObjectRepository.save(visitObject);
                }
            }
        }

        // Повторная вставка цен (перезапись)
        if (dto.getPrice() != null) {
            for (PriceDto priceDto : dto.getPrice()) {
                PriceEntity priceEntity = new PriceEntity();
                priceEntity.setService(entity); // связь с услугой

                if (priceDto.getVisitObjectId() != null) {
                    VisitObjectEntity visitObject = visitObjectRepository.findById(priceDto.getVisitObjectId())
                            .orElseThrow(() -> new RuntimeException("VisitObject не найден: " + priceDto.getVisitObjectId()));
                    priceEntity.setVisitObject(visitObject);
                }

                if (priceDto.getCategoryVisitorId() != null) {
                    CategoryVisitorEntity category = categoryVisitorRepository.findById(priceDto.getCategoryVisitorId())
                            .orElseThrow(() -> new RuntimeException("CategoryVisitor не найдена: " + priceDto.getCategoryVisitorId()));
                    priceEntity.setCategoryVisitor(category);
                }

                priceEntity.setCost(priceDto.getCost());
                priceRepository.save(priceEntity);
            }
        }

        // Возврат обновлённого состояния
        return buildEditableServiceDto(entity);
    }




    @Transactional
    public void deleteService(Long id) {
        // Удаляем связанные цены
        priceRepository.deleteAllByServiceId(id);

        // Удаляем саму услугу
        serviceRepository.deleteById(id);
    }

    public ServiceDto findById(Long id) {
        return serviceRepository.findById(id)
                .map(serviceMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Услуга не найдена"));
    }


    private void applyDtoToEntity(ServiceCreateDto dto, ServiceEntity entity) {
        entity.setServiceName(dto.getServiceName());
        entity.setDescription(dto.getDescription());
        entity.setCost(dto.getCost());
        entity.setActiveKindId(dto.getActiveKindId());
        entity.setIsNeedVisitDate(dto.getIsNeedVisitDate());
        entity.setIsNeedVisitTime(dto.getIsNeedVisitTime());
        entity.setDtBegin(dto.getDtBegin());
        entity.setDtEnd(dto.getDtEnd());
        entity.setProCultureIdentifier(dto.getProCultureIdentifier());
        entity.setIsPROCultureChecked(dto.getIsPROCultureChecked());
        entity.setIsDisableEditVisitObject(dto.getIsDisableEditVisitObject());
        entity.setIsDisableEditVisitor(dto.getIsDisableEditVisitor());
        entity.setIsVisitObjectUseForCost(dto.getIsVisitObjectUseForCost());
        entity.setIsCategoryVisitorUseForCost(dto.getIsCategoryVisitorUseForCost());
        entity.setIsVisitorCountUseForCost(dto.getIsVisitorCountUseForCost());
        entity.setUseOneCategory(dto.getIsUseOneCategory());
    }

    private EditableServiceDto buildEditableServiceDto(ServiceEntity service) {
        // Маппим основную сущность ServiceEntity в DTO-объект EditableServiceDto
        EditableServiceDto dto = editableServiceMapper.toDto(service);

        // Получаем ID всех VisitObject, связанных с данной услугой (используются для отметки isRequire)
        Set<Long> allowedIds = visitObjectRepository.findByServiceId(service.getId())
                .stream()
                .map(VisitObjectEntity::getId)
                .collect(Collectors.toSet());

        // Загружаем все VisitObject из базы и маппим их в DTO (даже не связанные с этой услугой)
        List<VisitObjectDto> visitObjects = visitObjectMapper.toDtoList(visitObjectRepository.findAll());

        // Проходим по всем объектам и формируем список, где isRequire = true, если объект связан с услугой
        List<VisitObjectDto> objectsForService = visitObjects.stream()
                .map(objectDto -> VisitObjectDto.builder()
                        .visitObjectId(objectDto.getVisitObjectId())
                        .visitObjectName(objectDto.getVisitObjectName())
                        .isRequire(allowedIds.contains(objectDto.getVisitObjectId())) // true, если объект связан
                        .groupVisitObjectId(objectDto.getGroupVisitObjectId())
                        .build())
                .toList();

        // Устанавливаем список объектов посещения в DTO
        dto.setVisitObjects(objectsForService);

        // Получаем все цены, связанные с услугой, и маппим их в DTO
        List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));
        dto.setPrices(prices);

        // Загружаем все категории посетителей
        List<CategoryVisitorDto> allCategories = categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());

        // Получаем ID всех категорий, использованных в ценах
        Set<Long> categoryIdsInPrice = prices.stream()
                .map(PriceDto::getCategoryVisitorId)
                .collect(Collectors.toSet());

        // Формируем список категорий для услуги:
        // если категория участвует в цене, выставляем requireVisitorCount = 0, иначе null
        List<CategoryVisitorDto> categoriesForService = allCategories.stream()
                .map(visitorDto -> CategoryVisitorDto.builder()
                        .categoryVisitorId(visitorDto.getCategoryVisitorId())
                        .categoryVisitorName(visitorDto.getCategoryVisitorName())
                        .groupCategoryVisitorId(visitorDto.getGroupCategoryVisitorId())
                        .requireVisitorCount(categoryIdsInPrice.contains(visitorDto.getCategoryVisitorId()) ? 0 : null)
                        .build())
                .toList();

        // Устанавливаем категории в DTO
        dto.setCategoryVisitor(categoriesForService);

        // Добавляем сеансовую сетку, если используется в UI
        dto.setSeanceGrid(seanceGridMapper.toDtoList(seanceGridRepository.findAll()));

        return dto;
    }




}
