package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceAssemblerMapper {

    ServiceAssemblerMapper INSTANCE = Mappers.getMapper(ServiceAssemblerMapper.class);

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "comment")
    MuseumServiceItemDto serviceToDto(ServiceEntity entity);

    @Mapping(source = "id", target = "visitObjectId")
    @Mapping(source = "visitObjectName", target = "visitObjectName")
    VisitObjectItemDto visitObjectToDto(VisitObjectEntity entity);

    @Mapping(source = "id", target = "categoryVisitorId")
    @Mapping(source = "categoryVisitorName", target = "categoryVisitorName")
    CategoryVisitorDto categoryVisitorToDto(CategoryVisitorEntity entity);

    @Mapping(source = "visitObject.id", target = "visitObjectId")
    @Mapping(source = "categoryVisitor.id", target = "categoryVisitorId")
    @Mapping(source = "cost", target = "cost")
    PriceDto priceToDto(PriceEntity entity);

    @Mapping(source = "dtBegin", target = "dtBegin")
    @Mapping(source = "dtEnd", target = "dtEnd")
    SeanceGridDto seanceToDto(SeanceGridEntity entity);

    List<VisitObjectItemDto> visitObjectListToDto(List<VisitObjectEntity> list);

    List<CategoryVisitorDto> categoryVisitorListToDto(List<CategoryVisitorEntity> list);

    List<PriceDto> priceListToDto(List<PriceEntity> list);

    List<SeanceGridDto> seanceListToDto(List<SeanceGridEntity> list);
}
