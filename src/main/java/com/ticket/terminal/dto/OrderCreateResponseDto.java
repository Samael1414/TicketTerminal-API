/**
 * DTO-ответ для операций создания заказа (Order).
 * 
 * Назначение:
 * - Используется для передачи информации о результате создания заказа между backend и клиентом/интеграцией.
 * - Возвращается контроллерами после успешного создания заказа (simple/editable).
 * 
 * Содержит данные:
 * - id созданного заказа, статус, сообщения об ошибках/успехе, связанные данные заказа и др.
 */
package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderCreateResponseDto {

    @JsonProperty("OrderId")
    private Integer orderId;

    @JsonProperty("OrderBarcode")
    private String orderBarcode;

    @JsonProperty("OrderStateId")
    private Integer orderStateId;

    @JsonProperty("OderSiteId")
    private String orderSiteId;

    @JsonProperty("Comment")
    private String comment;

    @JsonProperty("VisitorSiteId")
    private String visitorSiteId;

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

    @JsonProperty("VisitorAddress")
    private String visitorAddress;

    @JsonProperty("VisitorDocumentName")
    private String visitorDocumentName;

    @JsonProperty("VisitorDocumentSerial")
    private String visitorDocumentSerial;

    @JsonProperty("VisitorDocumentNumber")
    private String visitorDocumentNumber;

    @JsonProperty("Service")
    private List<OrderServiceDto> service;

    private SoldOrderRequestDto soldRequest;

}
