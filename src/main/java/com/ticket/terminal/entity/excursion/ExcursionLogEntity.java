package com.ticket.terminal.entity.excursion;

import com.ticket.terminal.entity.ServiceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "excursion_log")
public class ExcursionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long excursionLogInternetNumber;

    private String orgName;

    private String contactPersonName;

    private String contactPersonPhone;

    private String contactPersonMail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @Column(name = "dt_date", nullable = false)
    private LocalDateTime dtDate;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
