package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pro_culture_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProCultureLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderServiceId;

    private Long orderId;

    private Long seatId;

    @Column(nullable = false)
    private String proCultureGuid;
}
