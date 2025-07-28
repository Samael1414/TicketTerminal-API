package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
import com.ticket.terminal.dto.editable.EditableServiceDto;
import com.ticket.terminal.dto.editable.EditableServiceResponseDto;
import com.ticket.terminal.dto.service.ServiceCreateDto;
import com.ticket.terminal.dto.service.ServiceUpdateDto;
import com.ticket.terminal.dto.simple.SimpleServiceDto;
import com.ticket.terminal.dto.simple.SimpleServiceResponseDto;
import com.ticket.terminal.dto.visit.VisitObjectDto;
import com.ticket.terminal.dto.visit.VisitObjectItemDto;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.mapper.*;
import com.ticket.terminal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        List<SeanceGridDto> allSeanceGrid = seanceGridMapper.toDtoList(seanceGridRepository.findAll());

        List<SimpleServiceDto> services = serviceRepository.findAllSimpleServices()
                .stream()
                .map(entity -> {
                    SimpleServiceDto dto = serviceMapper.toSimpleDto(entity);
                    dto.setSeanceGrid(allSeanceGrid);
                    return dto;
                })
                .toList();

        return SimpleServiceResponseDto.builder()
                .service(services)
                .seanceGrid(allSeanceGrid)
                .build();
    }

    public EditableServiceResponseDto getEditableServices() {
        // 1) Загружаем «справочники» — они повторяются в ответе
        List<SeanceGridDto> allSeanceGrid = seanceGridMapper.toDtoList(seanceGridRepository.findAll());
        List<VisitObjectItemDto> allVisitObjects = visitObjectRepository.findAll()
                .stream()
                .map(this::toVisitObjectItemDto)
                .toList();
        List<CategoryVisitorDto> allCategories = categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());

        // 2) Обрабатываем каждую услугу
        List<EditableServiceDto> services = serviceRepository.findAllEditableServices()
                .stream()
                .map(service -> {
                    EditableServiceDto dto = editableServiceMapper.toDto(service);

                    // a) Визит-объекты, связанные напрямую и по прайсам
                    List<VisitObjectDto> visitObjectsForService = buildVisitObjectDtos(service);
                    dto.setVisitObjects(visitObjectsForService);

                    // b) Цены
                    List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));
                    dto.setPrices(prices);

                    // c) Категории, участвующие в ценах
                    List<CategoryVisitorDto> catForService = buildCategoryVisitorDtos(prices);
                    dto.setCategoryVisitor(catForService);

                    // d) Сеансовая сетка
                    dto.setSeanceGrid(allSeanceGrid);

                    return dto;
                })
                .toList();

        // 3) Собираем итог
        return EditableServiceResponseDto.builder()
                .visitObjects(allVisitObjects)
                .categoryVisitor(allCategories)
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
        // 1) Создаём и сохраняем новую сущность Service
        ServiceEntity entity = createAndSaveServiceEntity(dto);

        // 2) Связываем VisitObject по флагу isRequire
        bindVisitObjects(entity, dto.getVisitObject());

        // 3) Создаём записи Price
        bindPrices(entity, dto.getPrice());

        // 4) Возвращаем DTO с уже всеми связанными данными
        return buildEditableServiceDto(entity);
    }

    /**
     * Обновление существующей услуги по ID.
     * Перезаписывает сущность, заново связывает объекты посещения и цены.
     */
    @Transactional
    public EditableServiceDto updateService(Long id, ServiceUpdateDto dto) {
        // 1) Загружаем существующую сущность
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Услуга не найдена: " + id));

        // 2) Обновляем поля и сохраняем
        serviceMapper.updateEntity(entity, dto);
        serviceRepository.save(entity);

        // 3) Пересобираем связи: сначала чистим
        priceRepository.deleteAllByServiceId(id);
        if (!Boolean.TRUE.equals(entity.getIsDisableEditVisitObject())) {
            unbindAllVisitObjects(entity);
        }

        // 4) Затем заново «привязываем» VisitObject и Price
        bindVisitObjects(entity, dto.getVisitObject());
        bindPrices(entity, dto.getPrice());

        // 5) Снова строим DTO уже с только что сохранёнными связями
        return buildServiceDtoWithRelatedDataOnly(entity);
    }

    @Transactional
    public void deleteService(Long id) {
        // Удаляем связанные цены
        priceRepository.deleteAllByServiceId(id);

        // Удаляем саму услугу
        serviceRepository.deleteById(id);
    }

    public EditableServiceDto findById(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Услуга не найдена"));
        return buildServiceDtoWithRelatedDataOnly(serviceEntity);
    }

    /**
     * Создает DTO услуги только с данными, связанными с этой услугой
     * В отличие от buildEditableServiceDto, не включает все объекты и категории
     */
    private EditableServiceDto buildServiceDtoWithRelatedDataOnly(ServiceEntity service) {
        EditableServiceDto dto = editableServiceMapper.toDto(service);

        List<PriceDto> prices = loadPrices(service.getId());
        Set<Long> usedCatIds = extractCategoryIds(prices);

        dto.setVisitObjects(mapRelatedVisitObjects(service));
        dto.setPrices(prices);
        dto.setCategoryVisitor(mapCategoryVisitors(usedCatIds, false));
        dto.setSeanceGrid(loadAllSeanceGrid());

        return dto;
    }

    private EditableServiceDto buildEditableServiceDto(ServiceEntity service) {
        EditableServiceDto dto = editableServiceMapper.toDto(service);

        List<PriceDto> prices = loadPrices(service.getId());
        Set<Long> usedCatIds = extractCategoryIds(prices);

        dto.setVisitObjects(mapAllVisitObjectsForService(service));
        dto.setPrices(prices);
        dto.setCategoryVisitor(mapCategoryVisitors(usedCatIds, true));
        dto.setSeanceGrid(loadAllSeanceGrid());

        return dto;
    }

    private VisitObjectItemDto toVisitObjectItemDto(VisitObjectEntity visitObject) {
        return VisitObjectItemDto.builder()
                .visitObjectId(visitObject.getId())
                .visitObjectName(visitObject.getVisitObjectName())
                .categoryVisitorId(visitObject.getCategoryVisitorId())
                .address(visitObject.getAddress())
                .comment(visitObject.getComment())
                .build();
    }

    private List<VisitObjectDto> buildVisitObjectDtos(ServiceEntity service) {
        // напрямую связанные
        List<VisitObjectEntity> direct = visitObjectRepository.findAllByServiceId(service.getId());

        // связанные через price
        Set<Long> fromPricesIds = priceRepository.findAllByServiceId(service.getId())
                .stream()
                .map(price -> Optional.ofNullable(price.getVisitObject()).map(VisitObjectEntity::getId))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        List<VisitObjectEntity> fromPrices = visitObjectRepository.findAllById(fromPricesIds)
                .stream()
                .filter(visitObject -> direct.stream().noneMatch(d -> d.getId().equals(visitObject.getId())))
                .toList();

        // объединяем и мапим
        return Stream.concat(direct.stream(), fromPrices.stream())
                .map(visitObject -> VisitObjectDto.builder()
                        .visitObjectId(visitObject.getId())
                        .visitObjectName(visitObject.getVisitObjectName())
                        .isRequire(direct.contains(visitObject))
                        .groupVisitObjectId(visitObject.getGroupVisitObjectId())
                        .categoryVisitorId(visitObject.getCategoryVisitorId())
                        .address(visitObject.getAddress())
                        .comment(visitObject.getComment())
                        .build())
                .toList();
    }

    private List<CategoryVisitorDto> buildCategoryVisitorDtos(List<PriceDto> prices) {
        Set<Long> catIds = prices.stream()
                .map(PriceDto::getCategoryVisitorId)
                .collect(Collectors.toSet());

        return categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAllById(catIds))
                .stream()
                .map(dto -> CategoryVisitorDto.builder()
                        .categoryVisitorId(dto.getCategoryVisitorId())
                        .categoryVisitorName(dto.getCategoryVisitorName())
                        .groupCategoryVisitorId(dto.getGroupCategoryVisitorId())
                        .requireVisitorCount(0)
                        .build())
                .toList();
    }

    private ServiceEntity createAndSaveServiceEntity(ServiceCreateDto dto) {
        ServiceEntity entity = serviceMapper.toEntity(dto);
        return serviceRepository.save(entity);
    }


    private void bindVisitObjects(ServiceEntity service, List<VisitObjectDto> visitObjectDtos) {
        Optional.ofNullable(visitObjectDtos).orElse(List.of()).stream()
                .filter(VisitObjectDto::getIsRequire)
                .map(VisitObjectDto::getVisitObjectId)
                .filter(Objects::nonNull)
                .forEach(voId -> {
                    VisitObjectEntity entity = visitObjectRepository.findById(voId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Объект посещения не найден: " + voId));
                    entity.setService(service);
                    visitObjectRepository.save(entity);
                });
    }

    private void unbindAllVisitObjects(ServiceEntity service) {
        visitObjectRepository.findAllByServiceId(service.getId()).stream()
                .forEach(visitObject -> {
                    visitObject.setService(null);
                    visitObjectRepository.save(visitObject);
                });
    }

    private void bindPrices(ServiceEntity service, List<PriceDto> prices) {
        Optional.ofNullable(prices).orElse(List.of()).forEach(priceDto -> {
            PriceEntity priceEntity = new PriceEntity();
            priceEntity.setService(service);
            // visitObject
            if (priceDto.getVisitObjectId() != null) {
                VisitObjectEntity visitObjectEntity = visitObjectRepository.findById(priceDto.getVisitObjectId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Объект посещения не найден: " + priceDto.getVisitObjectId()));
                priceEntity.setVisitObject(visitObjectEntity);
            }
            // categoryVisitor
            if (priceDto.getCategoryVisitorId() != null) {
                CategoryVisitorEntity categoryVisitorEntity = categoryVisitorRepository.findById(priceDto.getCategoryVisitorId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Категория посетителя не найдена: " + priceDto.getCategoryVisitorId()));
                priceEntity.setCategoryVisitor(categoryVisitorEntity);
            }
            priceEntity.setCost(priceDto.getCost());
            priceRepository.save(priceEntity);
        });
    }

    /**
     * Загружает всю сеансовую сетку один раз
     */
    private List<SeanceGridDto> loadAllSeanceGrid() {
        return seanceGridMapper.toDtoList(seanceGridRepository.findAll());
    }

    /**
     * Цены по услуге
     */
    private List<PriceDto> loadPrices(Long serviceId) {
        return priceMapper.toDtoList(priceRepository.findAllByServiceId(serviceId));
    }

    /**
     * ID категорий, используемых в списке цен
     */
    private Set<Long> extractCategoryIds(List<PriceDto> prices) {
        return prices.stream()
                .map(PriceDto::getCategoryVisitorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Для EDITABLE: возвращает все VisitObjectDto (вместе с пометкой isRequire для
     * тех, кто непосредственно привязан к услуге)
     */
    private List<VisitObjectDto> mapAllVisitObjectsForService(ServiceEntity service) {
        Set<Long> directIds = visitObjectRepository.findAllByServiceId(service.getId())
                .stream()
                .map(VisitObjectEntity::getId)
                .collect(Collectors.toSet());

        // получаем все объекты и маркируем требуемые
        return visitObjectMapper.toDtoList(visitObjectRepository.findAll()).stream()
                .map(visitObjectDto -> VisitObjectDto.builder()
                        .visitObjectId(visitObjectDto.getVisitObjectId())
                        .visitObjectName(visitObjectDto.getVisitObjectName())
                        .isRequire(directIds.contains(visitObjectDto.getVisitObjectId()))
                        .groupVisitObjectId(visitObjectDto.getGroupVisitObjectId())
                        .categoryVisitorId(visitObjectDto.getCategoryVisitorId())
                        .address(visitObjectDto.getAddress())
                        .comment(visitObjectDto.getComment())
                        .build())
                .toList();
    }

    /**
     * Для «только связанные» (buildServiceDtoWithRelatedDataOnly):
     * объединяет прямые + через прайс связи
     */
    private List<VisitObjectDto> mapRelatedVisitObjects(ServiceEntity service) {
        // прямые объекты
        List<VisitObjectEntity> direct = visitObjectRepository.findAllByServiceId(service.getId());
        Set<Long> directIds = direct.stream().map(VisitObjectEntity::getId).collect(Collectors.toSet());

        // ID из прайсов
        Set<Long> priceIds = priceRepository.findAllByServiceId(service.getId()).stream()
                .map(PriceEntity::getVisitObject)
                .filter(Objects::nonNull)
                .map(VisitObjectEntity::getId)
                .collect(Collectors.toSet());

        // получаем сущности и фильтруем дубликаты
        List<VisitObjectEntity> fromPrices = visitObjectRepository.findAllById(priceIds).stream()
                .filter(visitObject -> !directIds.contains(visitObject.getId()))
                .toList();

        // объединяем
        List<VisitObjectEntity> all = new ArrayList<>(direct);
        all.addAll(fromPrices);

        // мапим в DTO
        return all.stream()
                .map(visitObject -> VisitObjectDto.builder()
                        .visitObjectId(visitObject.getId())
                        .visitObjectName(visitObject.getVisitObjectName())
                        .isRequire(directIds.contains(visitObject.getId()))
                        .groupVisitObjectId(visitObject.getGroupVisitObjectId())
                        .categoryVisitorId(visitObject.getCategoryVisitorId())
                        .address(visitObject.getAddress())
                        .comment(visitObject.getComment())
                        .build())
                .toList();
    }

    /**
     * Универсальный построитель списка категорий посетителя:
     * - includeAll=true  → возвращает все категории, с requireVisitorCount=0 для тех, что в usedIds, иначе null
     * - includeAll=false → возвращает только usedIds, с requireVisitorCount=0
     */
    private List<CategoryVisitorDto> mapCategoryVisitors(Set<Long> usedIds, boolean includeAll) {
        if (includeAll) {
            return categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll()).stream()
                    .map(categoryVisitorDto -> CategoryVisitorDto.builder()
                            .categoryVisitorId(categoryVisitorDto.getCategoryVisitorId())
                            .categoryVisitorName(categoryVisitorDto.getCategoryVisitorName())
                            .groupCategoryVisitorId(categoryVisitorDto.getGroupCategoryVisitorId())
                            .requireVisitorCount(usedIds.contains(categoryVisitorDto.getCategoryVisitorId()) ? 0 : null)
                            .build())
                    .toList();
        } else {
            return categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAllById(usedIds)).stream()
                    .map(categoryVisitorDto -> CategoryVisitorDto.builder()
                            .categoryVisitorId(categoryVisitorDto.getCategoryVisitorId())
                            .categoryVisitorName(categoryVisitorDto.getCategoryVisitorName())
                            .groupCategoryVisitorId(categoryVisitorDto.getGroupCategoryVisitorId())
                            .requireVisitorCount(0)
                            .build())
                    .toList();
        }
    }


}