package com.ticket.terminal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderBarcode;

    @Column(nullable = false)
    private Integer orderStateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private String visitorName1;

    private String visitorName2;

    private String visitorName3;

    private String visitorPhone;

    private String visitorMail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderServiceEntity> service;

    @Column(name = "is_simple_mode", nullable = false)
    private Boolean isSimpleMode;

    private String visitorAddress;

    private String visitorDocumentName;

    private String visitorDocumentSerial;

    private String visitorDocumentNumber;

    private String orderSiteId;

    private String visitorSiteId;

    private String comment;

    private Integer cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private OffsetDateTime dtDrop;
}
