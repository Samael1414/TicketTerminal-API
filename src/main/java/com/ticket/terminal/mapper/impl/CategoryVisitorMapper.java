//package com.ticket.terminal.mapper.impl;
//
//import com.ticket.terminal.dto.CategoryVisitorCountDto;
//import com.ticket.terminal.entity.OrderServiceVisitorEntity;
//import org.mapstruct.Named;
//import org.springframework.stereotype.Component;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class CategoryVisitorMapper {
//
//    @Named("mapVisitors")
//    public List<CategoryVisitorCountDto> mapVisitors(List<OrderServiceVisitorEntity> visitors) {
//        if (visitors == null) return List.of();
//        return visitors.stream()
//                .map(v -> new CategoryVisitorCountDto(
//                        v.getCategoryVisitor().getId(),
//                        v.getVisitorCount()))
//                .collect(Collectors.toList());
//    }
//}
