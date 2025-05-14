package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.SeanceGridEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.mapper.*;
import com.ticket.terminal.repository.*;
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
        List<VisitObjectDto> visitObjects = visitObjectMapper.toDtoList(visitObjectRepository.findAll());

        List<VisitObjectItemDto> visitObjectItems = visitObjects.stream()
                .map(item -> VisitObjectItemDto.builder()
                        .visitObjectId(item.getVisitObjectId())
                        .categoryVisitorId(item.getCategoryVisitorId())
                        .address(item.getAddress())
                        .comment(item.getComment())
                        .visitObjectName(item.getVisitObjectName())
                        .build())
                .toList();

        List<GroupVisitObjectDto> groupVisitObjects = visitObjects.stream()
                .map(objectDto -> GroupVisitObjectDto.builder()
                        .groupVisitObjectId(objectDto.getVisitObjectId())
                        .groupVisitObjectName(objectDto.getVisitObjectName())
                        .build())
                .toList();

        List<CategoryVisitorDto> allCategories = categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());
        List<GroupCategoryVisitorDto> groupCategoryVisitors = allCategories.stream()
                .map(categoryVisitorDto -> GroupCategoryVisitorDto.builder()
                        .groupCategoryVisitorId(categoryVisitorDto.getGroupCategoryVisitorId())
                        .groupCategoryVisitorName(categoryVisitorDto.getCategoryVisitorName())
                        .build())
                .toList();

        List<SeanceGridDto> allSeanceGrid = seanceGridMapper.toDtoList(seanceGridRepository.findAll());

        List<EditableServiceDto> services;
        try (Stream<ServiceEntity> stream = serviceRepository.findAllEditableServices().stream()) {
            services = stream.map(service -> {
                EditableServiceDto dto = editableServiceMapper.toDto(service);

                Set<Long> allowedIds = visitObjectRepository.findByServiceId(service.getId())
                        .stream()
                        .map(VisitObjectEntity::getId)
                        .collect(Collectors.toSet());

                List<VisitObjectDto> objectsForService = visitObjects.stream()
                        .map(objectDto -> VisitObjectDto.builder()
                                .visitObjectId(objectDto.getVisitObjectId())
                                .visitObjectName(objectDto.getVisitObjectName())
                                .isRequire(allowedIds.contains(objectDto.getVisitObjectId()))
                                .groupVisitObjectId(objectDto.getGroupVisitObjectId())
                                .build())
                        .toList();
                dto.setVisitObjects(objectsForService);

                List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));
                dto.setPrices(prices);

                Set<Long> categoryIdsInPrice = prices.stream()
                        .map(PriceDto::getCategoryVisitorId)
                        .collect(Collectors.toSet());

                List<CategoryVisitorDto> categoriesForService = allCategories.stream()
                        .map(visitorDto -> CategoryVisitorDto.builder()
                                .categoryVisitorId(visitorDto.getCategoryVisitorId())
                                .categoryVisitorName(visitorDto.getCategoryVisitorName())
                                .groupCategoryVisitorId(visitorDto.getGroupCategoryVisitorId())
                                .requireVisitorCount(categoryIdsInPrice.contains(visitorDto.getCategoryVisitorId()) ? 0 : null)
                                .build())
                        .toList();

                dto.setCategoryVisitor(categoriesForService);

                dto.setSeanceGrid(allSeanceGrid);
                return dto;
            }).toList();
        }

        return EditableServiceResponseDto.builder()
                .groupVisitObject(groupVisitObjects)
                .groupCategoryVisitor(groupCategoryVisitors)
                .visitObjects(visitObjectItems)
                .categoryVisitor(allCategories)
                .seanceGrid(allSeanceGrid)
                .service(services)
                .build();
    }

    public void createService(ServiceCreateDto dto) {
        ServiceEntity entity = new ServiceEntity();
        applyDtoToEntity(dto, entity);
        serviceRepository.save(entity);
    }

    public void updateService(Long id, ServiceUpdateDto dto) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service не найден"));
        applyDtoToEntity(dto, entity);
        serviceRepository.save(entity);
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


}
