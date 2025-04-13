package com.ticket.terminal.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldOrderResponseDto {

    private Long orderId;
    private String orderBarcode;
    private Integer orderStateId;
    private String orderSiteId;
    private String comment;
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
    private List<SoldServiceDto> service;
}
