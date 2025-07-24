package com.ticket.terminal.service;

import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
import com.ticket.terminal.dto.excursion.ExcursionDto;
import com.ticket.terminal.dto.excursion.ExcursionListResponseDto;
import com.ticket.terminal.dto.excursion.ExcursionRequestDto;
import com.ticket.terminal.dto.excursion.ExcursionResponseDto;
import com.ticket.terminal.dto.visit.VisitObjectDto;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.entity.excursion.ExcursionLogEntity;
import com.ticket.terminal.entity.excursion.ExcursionLogVisitObjectEntity;
import com.ticket.terminal.entity.excursion.ExcursionLogVisitorEntity;
import com.ticket.terminal.mapper.CategoryVisitorMapper;
import com.ticket.terminal.mapper.ExcursionMapper;
import com.ticket.terminal.mapper.PriceMapper;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.*;
import com.ticket.terminal.repository.excursion.ExcursionLogRepository;
import com.ticket.terminal.repository.excursion.ExcursionLogVisitObjectRepository;
import com.ticket.terminal.repository.excursion.ExcursionLogVisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcursionService {

    private final ServiceRepository serviceRepository;
    private final PriceRepository priceRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final ExcursionLogRepository excursionLogRepository;
    private final ExcursionLogVisitorRepository excursionLogVisitorRepository;
    private final ExcursionLogVisitObjectRepository excursionLogVisitObjectRepository;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    private final ExcursionMapper excursionMapper;
    private final VisitObjectMapper visitObjectMapper;
    private final CategoryVisitorMapper categoryVisitorMapper;
    private final PriceMapper priceMapper;

    public ExcursionListResponseDto getAllExcursions() {
        var excursions = loadAllExcursionDtos();
        var allVisitObjects = loadAllVisitObjects();
        var allCategories = loadAllCategories();
        return ExcursionListResponseDto.builder()
                .service(excursions)
                .visitObject(allVisitObjects)
                .categoryVisitor(allCategories)
                .build();
    }

    @Transactional
    public ExcursionResponseDto createExcursion(ExcursionRequestDto requestDto) {
        var log = buildLogEntity(requestDto);
        var saved = excursionLogRepository.save(log);

        saveVisitors(saved, requestDto.getVisitor());
        saveVisitObjects(saved, requestDto.getVisitObject());

        logAction("CREATE_EXCURSION", "Создана экскурсия: " + log.getService().getServiceName());
        return excursionMapper.toDto(saved);
    }


    private List<ExcursionDto> loadAllExcursionDtos() {
        // сгруппированные цены
        var priceMap = priceRepository.findAll().stream()
                .collect(Collectors.groupingBy(price -> price.getService().getId()));
        return serviceRepository.findAll().stream()
                .map(service -> mapServiceToExcursionDto(service, priceMap.get(service.getId())))
                .toList();
    }

    private ExcursionDto mapServiceToExcursionDto(ServiceEntity serviceEntity, List<PriceEntity> prices) {
        var dto = excursionMapper.toDto(serviceEntity);
        var list = Optional.ofNullable(prices).orElse(emptyList());
        dto.setPrices(list.stream().map(priceMapper::toDto).toList());
        dto.setVisitObjects(list.stream()
                .map(PriceEntity::getVisitObject)
                .filter(Objects::nonNull)
                .map(visitObjectMapper::toDto)
                .distinct()
                .toList());

        dto.setCategoryVisitors(list.stream()
                .map(PriceEntity::getCategoryVisitor)
                .filter(Objects::nonNull)
                .map(categoryVisitorMapper::toDto)
                .distinct()
                .toList());
        return dto;
    }

    private List<VisitObjectDto> loadAllVisitObjects() {
        return visitObjectMapper.toDtoList(visitObjectRepository.findAll());
    }

    private List<CategoryVisitorDto> loadAllCategories() {
        return categoryVisitorMapper.toDtoList(categoryVisitorRepository.findAll());
    }


    private ExcursionLogEntity buildLogEntity(ExcursionRequestDto requestDto) {
        var log = excursionMapper.toEntity(requestDto);
        var entity = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service не найдено: " + requestDto.getServiceId()));
        log.setService(entity);
        return log;
    }

    private void saveVisitors(ExcursionLogEntity log, List<CategoryVisitorCountDto> visitors) {
        Optional.ofNullable(visitors).orElse(emptyList()).stream()
                .forEach(visitorCountDto -> {
                    var categoryVisitor = categoryVisitorRepository.findById(visitorCountDto.getCategoryVisitorId())
                            .orElseThrow(() -> new EntityNotFoundException("CategoryVisitor не найдено: " + visitorCountDto.getCategoryVisitorId()));
                    excursionLogVisitorRepository.save(
                            ExcursionLogVisitorEntity.builder()
                                    .excursionLog(log)
                                    .categoryVisitor(categoryVisitor)
                                    .visitorCount(visitorCountDto.getVisitorCount())
                                    .build()
                    );
                });
    }

    private void saveVisitObjects(ExcursionLogEntity log, List<Long> visits) {
        Optional.ofNullable(visits).orElse(emptyList()).stream()
                .forEach(id -> {
                    var visitObjectEntity = visitObjectRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("VisitObject не найдено: " + id));
                    excursionLogVisitObjectRepository.save(
                            ExcursionLogVisitObjectEntity.builder()
                                    .excursionLog(log)
                                    .visitObject(visitObjectEntity)
                                    .build()
                    );
                });
    }

    private void logAction(String type, String desc) {
        var user = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(user)
                .actionType(type)
                .description(desc)
                .createdAt(LocalDateTime.now())
                .actorName(user.getUserName())
                .build());
    }

    private UsersEntity getCurrentUser() {
        var name = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUserNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}

