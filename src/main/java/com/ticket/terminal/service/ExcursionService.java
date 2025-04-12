package com.ticket.terminal.service;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import com.ticket.terminal.mapper.CategoryVisitorMapper;
import com.ticket.terminal.mapper.ExcursionMapper;
import com.ticket.terminal.mapper.PriceMapper;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExcursionService {

    private final ServiceRepository serviceRepository;
    private final VisitObjectRepository visitObjectRepository;
    private final CategoryVisitorRepository categoryVisitorRepository;
    private final PriceRepository priceRepository;
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
        List<ServiceEntity> serviceEntities = serviceRepository.findAll();

        // Маппим каждую ServiceEntity в ExcursionDto (без вложенных списков)
        List<ExcursionDto> excursionDto = serviceEntities.stream()
                .map(excursionMapper::toDto)
                .toList();


        // берём PriceEntity и группируем по service_id (Пример: сгруппируем все цены по service_id)
        List<PriceEntity> allPrices = priceRepository.findAll();
        Map<Long, List<PriceEntity>> priceByServiceId = allPrices.stream()
                .collect(Collectors.groupingBy(pe -> pe.getService().getId()));

        // Теперь пробежимся по excursionDtos и добавим им соответствующие prices
        for (ExcursionDto dto : excursionDto) {
            Long serviceId = dto.getServiceId();
            List<PriceEntity> priceEntitiesForService = priceByServiceId.
                    getOrDefault(serviceId, Collections.emptyList());
            List<PriceDto> priceDtos = priceEntitiesForService.stream()
                    .map(priceMapper::toDto)
                    .toList();
            dto.setPrices(priceDtos);

            // Аналогично для visitObjects и categoryVisitors внутри ExcursionDto,
            // если нужно собрать "уникальные" visitObjects/categoryVisitors для каждой услуги
            // Можно, к примеру, достать из PriceEntity visitObject и categoryVisitor
            // и сформировать списки.
            Set<VisitObjectDto> vObjects = priceEntitiesForService.stream()
                    .map(PriceEntity::getVisitObject)
                    .filter(Objects::nonNull)
                    .map(visitObjectMapper::toDto)
                    .collect(Collectors.toSet());
            dto.setVisitObjects(new ArrayList<>(vObjects));

            Set<CategoryVisitorDto> cVisitors = priceEntitiesForService.stream()
                    .map(PriceEntity::getCategoryVisitor)
                    .filter(Objects::nonNull)
                    .map(categoryVisitorMapper::toDto)
                    .collect(Collectors.toSet());
            dto.setCategoryVisitors(new ArrayList<>(cVisitors));
        }
        // Загружаем "глобальные" списки VisitObject и CategoryVisitor (для верхнего уровня ответа)
        List<VisitObjectEntity> visitObjectEntities = visitObjectRepository.findAll();
        List<VisitObjectDto> visitObjectDtos = visitObjectEntities.stream()
                .map(visitObjectMapper::toDto)
                .collect(Collectors.toList());

        List<CategoryVisitorEntity> categoryVisitorEntities = categoryVisitorRepository.findAll();
        List<CategoryVisitorDto> categoryVisitorDtos = categoryVisitorEntities.stream()
                .map(categoryVisitorMapper::toDto)
                .collect(Collectors.toList());

        // Формируем итоговый объект ответа
        ExcursionListResponseDto response = new ExcursionListResponseDto();
        response.setService(excursionDto);
        response.setVisitObject(visitObjectDtos);
        response.setCategoryVisitor(categoryVisitorDtos);

        return response;
    }

    @Transactional
    public ExcursionResponseDto createExcursion(ExcursionRequestDto requestDto) {

        // Преобразуем DTO в сущность (без списков visitor/visitObject)
        ExcursionLogEntity excursionLog = excursionMapper.toEntity(requestDto);

        // Подтягиваем serviceEntity из таблицы 'services'
        ServiceEntity serviceEntity = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Service not found: %s", requestDto.getServiceId())));
        excursionLog.setService(serviceEntity);

        //  Сохраняем excursionLog (пока без visitor/object)
        ExcursionLogEntity savedLog = excursionLogRepository.save(excursionLog);

        // Обрабатываем visitor[]: создаём записи в excursion_log_visitor
        if (requestDto.getVisitor() != null) {
            for (CategoryVisitorCountDto visitorDto : requestDto.getVisitor()) {
                // Найти categoryVisitorEntity
                CategoryVisitorEntity catEntity = categoryVisitorRepository.findById(visitorDto.getCategoryVisitorId())
                        .orElseThrow(() -> new EntityNotFoundException
                                (String.format("CategoryVisitor not found: %s", visitorDto.getCategoryVisitorId())));

                // Создать ExcursionLogVisitorEntity
                ExcursionLogVisitorEntity elv = new ExcursionLogVisitorEntity();
                elv.setExcursionLog(savedLog);
                elv.setCategoryVisitor(catEntity);
                elv.setVisitorCount(visitorDto.getVisitorCount());

                excursionLogVisitorRepository.save(elv);
            }
        }

        // Обрабатываем visitObject[]: создаём записи в excursion_log_visit_object
        if (requestDto.getVisitObject() != null) {
            for (Long voId : requestDto.getVisitObject()) {
                VisitObjectEntity voEntity = visitObjectRepository.findById(voId)
                        .orElseThrow(() -> new EntityNotFoundException
                                (String.format("VisitObject not found: %s", voId)));

                ExcursionLogVisitObjectEntity objectEntity = new ExcursionLogVisitObjectEntity();
                objectEntity.setExcursionLog(savedLog);
                objectEntity.setVisitObject(voEntity);

                excursionLogVisitObjectRepository.save(objectEntity);
            }
        }
        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(new ActionLogDto(
                currentUser.getId().intValue(),
                "CREATE_EXCURSION",
                String.format("Создана экскурсия: %s", serviceEntity.getServiceName()),
                LocalDateTime.now(),
                currentUser.getUserName()
        ));

        // Формируем ответ
        return excursionMapper.toDto(savedLog);

    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
    }

}
