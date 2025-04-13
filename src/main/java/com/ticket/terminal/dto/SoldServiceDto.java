package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldServiceDto {

    private Long orderServiceId;
    private String barcode;
    private Integer serviceStateId;
    private Integer paymentKindId;
    private LocalDateTime dtActive;
}
