package com.ticket.terminal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "order_services")
public class OrderServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    // Связь с сервисом может быть null, если услуга была удалена
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
    
    // Поля для хранения данных услуги (используются существующие поля)
    @Column(name = "service_name")
    private String serviceName;

    @Column(nullable = false)
    private Double cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dtVisit;

    @Column(name = "service_state_id", nullable = false)
    private Integer serviceStateId;

    @Column(name = "service_count", nullable = false)
    private Integer serviceCount;

    @Column(name = "dt_drop")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dtDrop;

    private String proCultureGuid;


}
