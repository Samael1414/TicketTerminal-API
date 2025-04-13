package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    public Long id;
    private Integer orderId;
    private String paymentType;
    private Integer amount;
    private String status;
    private Integer transactionId;
    private LocalDateTime createdAt;
}
