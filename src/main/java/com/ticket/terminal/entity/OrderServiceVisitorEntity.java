package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "order_service_visitor")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceVisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_service_id", nullable = false)
    private OrderServiceEntity orderService;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_visitor_id", nullable = false)
    private CategoryVisitorEntity categoryVisitor;

    @Column(name = "visitor_count", nullable = false)
    private Long visitorCount;

    @Column(name = "category_visitor_name")
    private String categoryVisitorName;

}
