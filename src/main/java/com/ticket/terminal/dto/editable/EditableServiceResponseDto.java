package com.ticket.terminal.dto.editable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ticket.terminal.dto.group.GroupCategoryVisitorDto;
import com.ticket.terminal.dto.group.GroupVisitObjectDto;
import com.ticket.terminal.dto.SeanceGridDto;
import com.ticket.terminal.dto.visit.VisitObjectItemDto;
import com.ticket.terminal.dto.category.CategoryVisitorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EditableServiceResponseDto {

    @JsonProperty("Service")
    private List<EditableServiceDto> service;

    @JsonProperty("VisitObject")
    private List<VisitObjectItemDto> visitObjects;

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorDto> categoryVisitor;

    @JsonProperty("SeanceGrid")
    private List<SeanceGridDto> seanceGrid;

    @JsonProperty("GroupVisitObject")
    private List<GroupVisitObjectDto> groupVisitObject;

    @JsonProperty("GroupCategoryVisitor")
    private List<GroupCategoryVisitorDto> groupCategoryVisitor;
}


