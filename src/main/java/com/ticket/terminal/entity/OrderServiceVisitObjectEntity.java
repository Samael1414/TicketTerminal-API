package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "order_service_visit_object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceVisitObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_service_id", nullable = false)
    private OrderServiceEntity orderService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_object_id", nullable = false)
    private VisitObjectEntity visitObject;
}

