package com.ticket.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SimpleOrderRequestDto {

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
    private Integer cost;

    @JsonProperty("Comment")
    private String comment;

    private OffsetDateTime dtDrop;

    @JsonProperty("Service")
    private List<SimpleOrderServiceDto> service;
}
