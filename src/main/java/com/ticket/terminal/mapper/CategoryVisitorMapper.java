package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.CategoryVisitorDto;
import com.ticket.terminal.entity.CategoryVisitorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryVisitorMapper {

    @Mapping(source = "id", target = "categoryVisitorId")
    @Mapping(source = "categoryName", target = "categoryVisitorName")
    CategoryVisitorDto toDto(CategoryVisitorEntity CategoryVisitorEntity);

    CategoryVisitorEntity toEntity(CategoryVisitorDto CategoryVisitorDto);

    List<CategoryVisitorDto> toDtoList(List<CategoryVisitorEntity> entities);
}
