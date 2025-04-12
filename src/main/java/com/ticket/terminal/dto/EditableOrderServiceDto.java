package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditableOrderServiceDto {

    private Long serviceId;
    private Integer serviceCost;
    private Integer serviceCount;
    private OffsetDateTime dtVisit;
    private List<Long> visitObjectId;
    private List<CategoryVisitorCountDto> categoryVisitor;
}
