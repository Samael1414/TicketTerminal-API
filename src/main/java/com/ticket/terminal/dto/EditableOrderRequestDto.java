/**
 * DTO для создания редактируемого заказа (Editable Order).
 * 
 * Назначение:
 * - Используется для передачи параметров создания заказа с редактируемыми услугами от клиента к backend.
 * - Применяется в контроллерах и сервисах для создания заказа через API.
 * 
 * Содержит данные:
 * - список услуг, параметры редактирования, данные клиента, параметры оплаты и др.
 */
package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EditableOrderRequestDto {

    @JsonProperty("IsSimpleMode")
    private Boolean isSimpleMode;

    @JsonProperty("OrderSiteId")
    private String orderSiteId;

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

    @JsonProperty("Cost")
    private Double cost;

    @JsonProperty("Comment")
    private String comment;

    @JsonAlias("dtDrop")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dtDrop;

    @JsonProperty("Service")
    private List<EditableOrderServiceDto> service;
}
