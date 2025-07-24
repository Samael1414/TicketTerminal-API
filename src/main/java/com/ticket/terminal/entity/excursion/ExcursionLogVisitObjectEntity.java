package com.ticket.terminal.entity.excursion;

import com.ticket.terminal.entity.VisitObjectEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "excursion_log_visit_object")
public class ExcursionLogVisitObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на бронирование экскурсии
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excursion_log_id", nullable = false)
    private ExcursionLogEntity excursionLog;

    // Ссылка на объект посещения
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_object_id", nullable = false)
    private VisitObjectEntity visitObject;
}
