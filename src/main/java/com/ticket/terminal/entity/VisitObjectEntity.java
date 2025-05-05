package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "visit_objects")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VisitObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String visitObjectName;

    @Column(name = "is_required")
    private Boolean isRequire;
}
