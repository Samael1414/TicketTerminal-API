package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoldServiceDto {

    @JsonProperty("OrderServiceId")
    private Long orderServiceId;

    @JsonProperty("Barcode")
    private String barcode;

    @JsonProperty("ServiceStateId")
    private Integer serviceStateId;

    @JsonProperty("PaymentKindId")
    private Integer paymentKindId;

    private LocalDateTime dtActive;

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("VisitObjectId")
    private List<Long> visitObjectId;

    @JsonProperty("VisitObject")
    private List<Long> visitObject;

    @JsonProperty("Cost")
    private Double serviceCost;

    @JsonProperty("Count")
    private Integer serviceCount;

    @JsonProperty("DtVisit")
    private LocalDateTime dtVisit;

    @JsonProperty("Visitor")
    private List<CategoryVisitorCountDto> visitor = new ArrayList<>();

    @JsonProperty("CategoryVisitor")
    private List<CategoryVisitorCountDto> categoryVisitor = new ArrayList<>();


}
