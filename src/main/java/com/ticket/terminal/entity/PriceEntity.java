package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "prices")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "visit_object_id")
    private VisitObjectEntity visitObject;

    @ManyToOne
    @JoinColumn(name = "category_visitor_id")
    private CategoryVisitorEntity categoryVisitor;

    private Integer cost;
}
