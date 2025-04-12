package com.ticket.terminal.service;


import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.SeanceGridEntity;
import com.ticket.terminal.entity.ServiceEntity;
import com.ticket.terminal.mapper.*;
import com.ticket.terminal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<SimpleServiceDto> service = serviceRepository.findAll().stream()
                .map(serviceMapper::toSimpleDto)
                .toList();

        List<SeanceGridEntity> all = seanceGridRepository.findAll();
        List<SeanceGridDto> seanceGrid = all.stream()
                .map(seanceGridMapper::toDto)
                .toList();

        SimpleServiceResponseDto response = new SimpleServiceResponseDto();
        response.setServices(service);
        response.setSeanceGrid(seanceGrid);
        return response;
    }

    public List<EditableServiceDto> getEditableServices() {
        List<ServiceEntity> services = serviceRepository.findAllEditableServices();

        return services.stream()
                .map(service -> {
                    EditableServiceDto dto = editableServiceMapper.toDto(service);

                    List<VisitObjectDto> visitObject = visitObjectMapper
                            .toDtoList(visitObjectRepository.findByServiceId(service.getId()));

                    List<CategoryVisitorDto> categoryVisitors = categoryVisitorMapper.toDtoList(
                            categoryVisitorRepository.findByServiceId(service.getId()));

                    List<PriceDto> prices = priceMapper.toDtoList(priceRepository.findAllByServiceId(service.getId()));

                    dto.setVisitObjects(visitObject);
                    dto.setCategoryVisitors(categoryVisitors);
                    dto.setPrices(prices);

                    return dto;
                })
                .toList();
    }
}
