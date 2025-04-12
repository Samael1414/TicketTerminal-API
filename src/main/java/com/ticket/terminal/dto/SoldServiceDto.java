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
public class SoldServiceDto {

    private Long orderServiceId;
    private String barcode;
    private Integer serviceStateId;
    private Integer paymentKindId;
    private LocalDateTime dtActive;
}
