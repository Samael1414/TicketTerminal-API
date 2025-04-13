package com.ticket.terminal.dto;
/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderBarcode;
    private Integer orderStateId;
    private String orderSiteId;
    private String visitorName1;
    private String visitorName2;
    private String visitorName3;
    private String visitorPhone;
    private String visitorMail;
    private List<OrderServiceDto> service;
    private Integer cost;
}
