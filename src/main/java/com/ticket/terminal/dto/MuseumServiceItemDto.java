//package com.ticket.terminal.dto;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class MuseumServiceItemDto {
//
//    private Integer serviceId;
//    private String serviceName;
//    private String comment;
//
//    private Integer activeKindId;
//    private Boolean isNeedVisitDate;
//    private Boolean isNeedVisitTime;
//
//    private Integer activeDays;
//    private LocalDateTime dtBegin;
//    private LocalDateTime dtEnd;
//    private List<LocalDate> dates;
//
//    private List<VisitObjectItemDto> visitObject;
//    private List<CategoryVisitorDto> categoryVisitor;
//    private List<PriceDto> price;
//    private List<SeanceGridDto> seanceGrid;
//
//    private Integer maxVisitorCount;
//    private Integer maxVisitObjectCount;
//
//    private Boolean isDisableEditVisitor;
//    private Boolean isDisableEditVisitObject;
//    private Boolean isVisitObjectUseForCost;
//    private Boolean isCategoryVisitorUseForCost;
//    private Boolean isVisitorCountUseForCost;
//    private Boolean isUseOneCategory;
//
//    private Integer proCultureIdentifier;
//    private Boolean isPROCultureChecked;
//    private Integer paymentKindId;
//}
