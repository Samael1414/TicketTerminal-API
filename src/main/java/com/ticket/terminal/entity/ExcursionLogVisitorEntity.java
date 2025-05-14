package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "excursion_log_visitor")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionLogVisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excursion_log_id", nullable = false)
    private ExcursionLogEntity excursionLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_visitor_id", nullable = false)
    private CategoryVisitorEntity categoryVisitor;

    @Column(name = "visitor_count", nullable = false)
    private Long visitorCount;
}
