package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sold_services")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderServiceId;

    private String barcode;

    private Integer serviceStateId;

    private Integer paymentKindId;

    private LocalDateTime dtActive;
}
