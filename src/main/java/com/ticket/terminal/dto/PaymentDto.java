package com.ticket.terminal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    public Long id;
    private Integer orderId;
    private String paymentType;
    private Integer amount;
    private String status;
    private Integer transactionId;
    private LocalDateTime createdAt;
}
