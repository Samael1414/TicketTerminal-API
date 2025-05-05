package com.ticket.terminal.entity;

import com.ticket.terminal.enums.OrderStatus;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_barcode", nullable = false, unique = true)
    private String orderBarcode;

    @Column(nullable = false)
    private Integer orderStateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private String visitorName1;
    private String visitorName2;
    private String visitorName3;
    private String visitorPhone;
    private String visitorMail;

    @CreatedDate
    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

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
    private Double cost;
    private LocalDateTime dtDrop;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @PrePersist
    public void prePersist() {
        if (this.orderBarcode == null || this.orderBarcode.isEmpty()) {
            this.orderBarcode = BarcodeGeneratorUtil.generateOrderBarcode();
        }
        if (this.orderStateId == null) {
            this.orderStateId = ServiceState.ORDERED.getCode();
        }
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.NEW;
        }

    }

    public void setOrderBarcode(String orderBarcode) {
        log.error("⚠️ SET orderBarcode → {}", orderBarcode);
        this.orderBarcode = orderBarcode;
    }


}
