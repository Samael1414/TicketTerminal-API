package com.ticket.terminal.dto;

/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
