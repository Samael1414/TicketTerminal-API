package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
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
}
