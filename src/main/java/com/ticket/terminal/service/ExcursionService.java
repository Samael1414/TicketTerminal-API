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
        //TODO: переделать все такие места где stream вызывается на объекте полученном из бд путем findAll на Try with Resources. погугли че да как

        // берём PriceEntity и группируем по service_id (Пример: сгруппируем все цены по service_id)
        List<PriceEntity> allPrices = priceRepository.findAll();
        Map<Long, List<PriceEntity>> priceByServiceId = allPrices.stream()
                .collect(Collectors.groupingBy(pe -> pe.getService().getId()));
        //TODO: переделать все такие места где stream вызывается на объекте полученном из бд путем findAll на Try with Resources. погугли че да как

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
        return ExcursionListResponseDto.builder()
                .service(excursionDto)
                .visitObject(visitObjectDtos)
                .categoryVisitor(categoryVisitorDtos)
                .build();
    }

    @Transactional
    public ExcursionResponseDto createExcursion(ExcursionRequestDto requestDto) {

        // Преобразуем DTO в сущность (без списков visitor/visitObject)
        ExcursionLogEntity excursionLog = excursionMapper.toEntity(requestDto);

        // Подтягиваем serviceEntity из таблицы 'services'
        ServiceEntity serviceEntity = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Service не найдено: %s", requestDto.getServiceId())));
        excursionLog.setService(serviceEntity);

        //  Сохраняем excursionLog (пока без visitor/object)
        ExcursionLogEntity savedLog = excursionLogRepository.save(excursionLog);

        // Обрабатываем visitor[]: создаём записи в excursion_log_visitor
        if (requestDto.getVisitor() != null) {
            for (CategoryVisitorCountDto visitorDto : requestDto.getVisitor()) {
                // Найти categoryVisitorEntity
                CategoryVisitorEntity catEntity = categoryVisitorRepository.findById(visitorDto.getCategoryVisitorId())
                        .orElseThrow(() -> new EntityNotFoundException
                                (String.format("CategoryVisitor не найдено: %s", visitorDto.getCategoryVisitorId())));

                excursionLogVisitorRepository.save(ExcursionLogVisitorEntity.builder()
                        .excursionLog(savedLog)
                        .categoryVisitor(catEntity)
                        .visitorCount(visitorDto.getVisitorCount())
                        .build());
            }
        }

        // Обрабатываем visitObject[]: создаём записи в excursion_log_visit_object
        if (requestDto.getVisitObject() != null) {
            for (Long visit : requestDto.getVisitObject()) {
                VisitObjectEntity visitObjectEntity = visitObjectRepository.findById(visit)
                        .orElseThrow(() -> new EntityNotFoundException
                                (String.format("VisitObject не найдено: %s", visit)));

                ExcursionLogVisitObjectEntity objectEntity = new ExcursionLogVisitObjectEntity();
                objectEntity.setExcursionLog(savedLog);
                objectEntity.setVisitObject(visitObjectEntity);

                excursionLogVisitObjectRepository.save(objectEntity);
            }
        }
        UsersEntity currentUser = getCurrentUser();
        actionLogService.save(ActionLogEntity.builder()
                .user(currentUser)
                .actionType("CREATE_EXCURSION")
                .description(String.format("Создана экскурсия: %s", serviceEntity.getServiceName()))
                .createdAt(LocalDateTime.now())
                .actorName(currentUser.getUserName())
                .build());
        // Формируем ответ
        return excursionMapper.toDto(savedLog);

    }

    private UsersEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }

}
