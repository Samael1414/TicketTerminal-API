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
public class PaymentDto {

    @JsonProperty("Id")
    public Long id;

    @JsonProperty("OrderId")
    private Integer orderId;

    @JsonProperty("PaymentType")
    private String paymentType;

    @JsonProperty("Amount")
    private Integer amount;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("TransactionId")
    private Integer transactionId;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
}
