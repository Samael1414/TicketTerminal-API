package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleOrderRequestDto {

    @JsonProperty("OrderServiceId")
    private Long orderServiceId;

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
    private List<SimpleOrderServiceDto> service;

    @JsonProperty("PaymentKindId")
    private Integer paymentKindId;

    @JsonProperty("PaymentTransaction")
    private String paymentTransaction;
}
