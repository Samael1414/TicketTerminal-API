package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldOrderRequestDto {

    private Integer isSimpleMode;
    private Integer isOnlyCheck;
    private Long orderId;
    private Integer paymentKindId;
    private String paymentTransaction;
    private String orderSiteId;
    private String visitorSiteId;
    private String visitorName1;
    private String visitorName2;
    private String visitorName3;
    private String visitorPhone;
    private String visitorMail;
    private String visitorAddress;
    private String visitorDocumentName;
    private String visitorDocumentSerial;
    private String visitorDocumentNumber;
    private String comment;
    private Integer cost;
    private List<SoldServiceDto> service;
}
