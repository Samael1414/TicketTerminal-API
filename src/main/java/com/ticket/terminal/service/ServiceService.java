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
//    private final MuseumServiceAssembler museumServiceAssembler;

    public SimpleServiceResponseDto getSimpleService() {
        List<SeanceGridDto> allSeanceGrid;
        try (Stream<SeanceGridEntity> gridStream = seanceGridRepository.findAll().stream()){
            allSeanceGrid = gridStream
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
                        .visitObjectName(item.getVisitObjectName())
                        .build())
                .toList();

        List<GroupVisitObjectDto> groupVisitObjects = visitObjects.stream()
                .map(obj -> GroupVisitObjectDto.builder()
                        .groupVisitObjectId(obj.getVisitObjectId())
                        .groupVisitObjectName(obj.getVisitObjectName())
                        .build())
                .toList();

        List<CategoryVisitorDto> allCategories = categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());
        List<GroupCategoryVisitorDto> groupCategoryVisitors = allCategories.stream()
                .map(cat -> GroupCategoryVisitorDto.builder()
                        .groupCategoryVisitorId(cat.getGroupCategoryVisitorId())
                        .groupCategoryVisitorName(cat.getCategoryVisitorName())
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
                        .map(vo -> VisitObjectDto.builder()
                                .visitObjectId(vo.getVisitObjectId())
                                .visitObjectName(vo.getVisitObjectName())
                                .isRequire(allowedIds.contains(vo.getVisitObjectId()))
                                .groupVisitObjectId(vo.getGroupVisitObjectId())
                                .build())
                        .toList();
                dto.setVisitObjects(objectsForService);

                List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));
                dto.setPrices(prices);

                Set<Long> categoryIdsInPrice = prices.stream()
                        .map(PriceDto::getCategoryVisitorId)
                        .collect(Collectors.toSet());

                List<CategoryVisitorDto> categoriesForService = allCategories.stream()
                        .map(cat -> CategoryVisitorDto.builder()
                                .categoryVisitorId(cat.getCategoryVisitorId())
                                .categoryVisitorName(cat.getCategoryVisitorName())
                                .groupCategoryVisitorId(cat.getGroupCategoryVisitorId())
                                .requireVisitorCount(categoryIdsInPrice.contains(cat.getCategoryVisitorId()) ? 0 : null)
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




//    public TLMuseumServiceResponseDto getFullMuseumServiceResponse() {
//        return museumServiceAssembler.getFullMuseumService();
//    }


}
