package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.VisitObjectEntity;
import com.ticket.terminal.mapper.ServiceAssemblerMapper;
import com.ticket.terminal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MuseumServiceAssembler {

    private final ServiceRepository serviceRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final PriceRepository priceRepository;
    private final SeanceGridRepository seanceGridRepository;
    private final ServiceAssemblerMapper mapper;

    public TLMuseumServiceResponseDto getFullMuseumService() {
        List<VisitObjectItemDto> visitObjects = mapper.visitObjectListToDto(visitObjectRepository.findAll());
        List<CategoryVisitorDto> categoryVisitors = mapper.categoryVisitorListToDto(categoryVisitorRepository.findAll());
        List<SeanceGridDto> seanceGrids = mapper.seanceListToDto(seanceGridRepository.findAll());

        List<MuseumServiceItemDto> services = serviceRepository.findAllEditableServices()
                .stream()
                .map(service -> {
                    MuseumServiceItemDto dto = mapper.serviceToDto(service);

                    Set<Long> allowedVisitObjectIds = visitObjectRepository.findByServiceId(service.getId())
                            .stream()
                            .map(VisitObjectEntity::getId)
                            .collect(Collectors.toSet());

                    List<VisitObjectItemDto> serviceVisitObjects = visitObjects.stream()
                            .map(visitObjectItemDto -> VisitObjectItemDto.builder()
                                    .visitObjectId(visitObjectItemDto.getVisitObjectId())
                                    .visitObjectName(visitObjectItemDto.getVisitObjectName())
                                    .isRequire(allowedVisitObjectIds.contains(visitObjectItemDto.getVisitObjectId()))
                                    .groupVisitObjectId(visitObjectItemDto.getVisitObjectId())
                                    .build())
                            .toList();

                    dto.setVisitObject(serviceVisitObjects);
                    dto.setCategoryVisitor(mapper.categoryVisitorListToDto(categoryVisitorRepository.findByServiceId(service.getId())));
                    dto.setPrice(mapper.priceListToDto(priceRepository.findAllByServiceId(service.getId())));
                    dto.setSeanceGrid(seanceGrids);

                    return dto;
                })
                .toList();

        return TLMuseumServiceResponseDto.builder()
                .service(services)
                .visitObject(visitObjects)
                .categoryVisitor(categoryVisitors)
                .seanceGrid(seanceGrids)
                .build();
    }
}
