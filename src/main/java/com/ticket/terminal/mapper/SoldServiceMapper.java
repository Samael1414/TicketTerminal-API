package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.SoldServiceDto;
import com.ticket.terminal.entity.SoldServiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SoldServiceMapper {

    SoldServiceDto toDto(SoldServiceEntity serviceEntity);

    SoldServiceEntity toEntity(SoldServiceDto serviceDto);
}
