package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.PriceDto;
import com.ticket.terminal.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    @Mapping(source = "visitObject.id", target = "visitObjectId")
    @Mapping(source = "categoryVisitor.id", target = "categoryVisitorId")
    PriceDto toDto(PriceEntity priceEntity);

    // Маппим DTO -> Entity, service маппим вручную (setService)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "visitObject", ignore = true)
    @Mapping(target = "categoryVisitor", ignore = true)
    PriceEntity toEntity(PriceDto priceDto);

    List<PriceDto> toDtoList(List<PriceEntity> entities);
}
