package com.ticket.terminal.mapper;

import com.ticket.terminal.dto.EditableServiceDto;
import com.ticket.terminal.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditableServiceMapper {

    @Mapping(source = "id" , target = "serviceId")
    @Mapping(source = "description", target = "comment")
    @Mapping(source = "needVisitDate", target = "needVisitDate")
    @Mapping(source = "needVisitTime", target = "needVisitTime")
    @Mapping(source = "proCultureChecked", target = "proCultureChecked")
    EditableServiceDto toDto(ServiceEntity serviceEntity);

    @Mapping(source = "serviceId" , target = "id")
    @Mapping(source = "comment", target = "description")
    @Mapping(source = "needVisitDate", target = "needVisitDate")
    @Mapping(source = "needVisitTime", target = "needVisitTime")
    @Mapping(source = "proCultureChecked", target = "proCultureChecked")
    ServiceEntity toEntity(EditableServiceDto editableServiceDto);
}
