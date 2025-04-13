package com.ticket.terminal.dto;
/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import lombok.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceDto {

    private Long orderServiceId;
    private Integer serviceStateId;
    private String serviceStateName;
    private Integer serviceId;
    private String serviceName;
    private Integer cost;
    private OffsetDateTime dtVisit;
    private Integer serviceCount;
    private OffsetDateTime dtDrop;

}
