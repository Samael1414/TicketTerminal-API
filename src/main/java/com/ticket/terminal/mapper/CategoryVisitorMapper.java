package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.category.CategoryVisitorCreateDto;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
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

    // 💡 Маппер для создания из DTO поле id игнорируется
    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "requireVisitorCount", ignore = true)
    CategoryVisitorEntity toEntity(CategoryVisitorDto dto);

    // 💡 Маппер для создания из CreateDto
    CategoryVisitorEntity toEntity(CategoryVisitorCreateDto createDto);
}
