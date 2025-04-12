package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "excursion_log_visitor")
public class ExcursionLogVisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на бронирование экскурсии
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excursion_log_id", nullable = false)
    private ExcursionLogEntity excursionLog;

    // Ссылка на категорию посетителей
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_visitor_id", nullable = false)
    private CategoryVisitorEntity categoryVisitor;

    // Количество посетителей для данной категории
    @Column(name = "visitor_count", nullable = false)
    private Long visitorCount;
}
