package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private List<OrderDto> order;
}
