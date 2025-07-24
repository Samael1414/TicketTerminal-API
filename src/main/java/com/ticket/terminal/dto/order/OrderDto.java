/**
 * DTO для передачи информации о заказе (Order) между слоями приложения и внешними системами.
 * 
 * Назначение:
 * - Представляет основные сведения о заказе: ID, статус, связанные услуги, пользователь и т.д.
 * - Используется в контроллерах, сервисах, клиентах и для обмена с внешними API.
 * 
 * Содержит данные:
 * - id заказа, статус, список услуг, информация о пользователе, даты создания/обновления и др.
 */
package com.ticket.terminal.dto.order;
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

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderDto {

    @JsonProperty("Id")
    private Long id;

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
    private Double cost;

    @JsonProperty("OrderId")
    private Integer orderId;

    @JsonProperty("Created")
    private LocalDateTime created;

}
