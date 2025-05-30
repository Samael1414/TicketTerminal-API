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

    @Column(name = "visit_object_name", nullable = false)
    private String visitObjectName;

    @Column(name = "is_required")
    private Boolean isRequire;

    @Column(name = "category_visitor_id")
    private Long categoryVisitorId;

    @Column(name = "address")
    private String address;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    // ➕ Метод для getGroupVisitObjectId() заглушкой
    public Long getGroupVisitObjectId() {
        return this.id; // если нет отдельного поля group_id — временно вернем id
    }

}
