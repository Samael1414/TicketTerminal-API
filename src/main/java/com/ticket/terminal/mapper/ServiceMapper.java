package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.ServiceDto;
import com.ticket.terminal.dto.SimpleServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "needVisitDate", target = "needVisitDate")
    @Mapping(source = "needVisitTime", target = "needVisitTime")
    @Mapping(source = "proCultureChecked", target = "proCultureChecked")
    ServiceDto toDto(ServiceEntity serviceEntity);

    ServiceEntity toEntity(ServiceDto serviceDto);

    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "description", target = "comment")
    @Mapping(source = "needVisitDate", target = "needVisitDate")
    @Mapping(source = "needVisitTime", target = "needVisitTime")
    @Mapping(source = "proCultureChecked", target = "proCultureChecked")
    SimpleServiceDto toSimpleDto(ServiceEntity entity);
}
