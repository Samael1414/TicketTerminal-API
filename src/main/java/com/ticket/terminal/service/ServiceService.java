package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.SeanceGridEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.mapper.*;
import com.ticket.terminal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        List<SimpleServiceDto> service;
        try (Stream<ServiceEntity> stream = serviceRepository.findAll().stream()) {
            service = stream.map(serviceMapper::toSimpleDto).toList();
        }

        List<SeanceGridDto> seanceGrid;
        try (Stream<SeanceGridEntity> stream = seanceGridRepository.findAll().stream()) {
            seanceGrid = stream.map(seanceGridMapper::toDto).toList();
        }

        return SimpleServiceResponseDto.builder().services(service).seanceGrid(seanceGrid).build();
    }

    public List<EditableServiceDto> getEditableServices() {
        try (Stream<ServiceEntity> stream = serviceRepository.findAllEditableServices().stream()) {
            return stream.map(service -> {
                EditableServiceDto dto = editableServiceMapper.toDto(service);

                List<VisitObjectDto> visitObject = visitObjectMapper
                        .toDtoList(visitObjectRepository.findByServiceId(service.getId()));

                List<CategoryVisitorDto> categoryVisitors = categoryVisitorMapper
                        .toDtoList(categoryVisitorRepository.findByServiceId(service.getId()));

                List<PriceDto> prices = priceMapper
                        .toDtoList(priceRepository.findAllByServiceId(service.getId()));

                dto.setVisitObjects(visitObject);
                dto.setCategoryVisitors(categoryVisitors);
                dto.setPrices(prices);

                return dto;
            }).toList();
        }
    }
}
