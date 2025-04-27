package com.ticket.terminal.dto;
/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderDto {

    @JsonProperty("OrderBarcode")
    private String orderBarcode;

    @JsonProperty("OrderStateId")
    private Integer orderStateId;

    @JsonProperty("OrderSiteId")
    private String orderSiteId;

    @JsonProperty("VisitorName1")
    private String visitorName1;

    @JsonProperty("VisitorName2")
    private String visitorName2;

    @JsonProperty("VisitorName3")
    private String visitorName3;

    @JsonProperty("VisitorPhone")
    private String visitorPhone;

    @JsonProperty("VisitorMail")
    private String visitorMail;

    @JsonProperty("Service")
    private List<OrderServiceDto> service;

    @JsonProperty("Cost")
    private Integer cost;
}
