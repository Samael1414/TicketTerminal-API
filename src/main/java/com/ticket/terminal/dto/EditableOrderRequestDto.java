package com.ticket.terminal.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditableOrderRequestDto {

    private Boolean isSimpleMode;
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
    private Integer cost;
    private String comment;
    private OffsetDateTime dtDrop;
    private List<EditableOrderServiceDto> service;
}
