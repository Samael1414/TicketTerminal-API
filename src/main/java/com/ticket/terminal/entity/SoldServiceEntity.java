package com.ticket.terminal.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sold_services")
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
