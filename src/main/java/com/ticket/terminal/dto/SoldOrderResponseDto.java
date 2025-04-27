package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SoldOrderResponseDto {

    @JsonProperty("OrderId")
    private Long orderId;

    @JsonProperty("OrderBarcode")
    private String orderBarcode;

    @JsonProperty("OrderStateId")
    private Integer orderStateId;

    @JsonProperty("OrderSiteId")
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
    private List<SoldServiceDto> service;
}
