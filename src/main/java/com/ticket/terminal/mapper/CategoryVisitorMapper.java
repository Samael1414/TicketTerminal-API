package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.CategoryVisitorCreateDto;
import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.entity.CategoryVisitorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryVisitorMapper {

    @Mapping(source = "id", target = "categoryVisitorId")
    @Mapping(source = "id", target = "groupCategoryVisitorId") // временно если нужны одинаковые
    CategoryVisitorDto toDto(CategoryVisitorEntity entity);

    List<CategoryVisitorDto> toDtoList(List<CategoryVisitorEntity> entities);

    CategoryVisitorEntity toEntity(CategoryVisitorDto dto);

    // 💡 Маппер для создания из CreateDto
    CategoryVisitorEntity toEntity(CategoryVisitorCreateDto createDto);
}
