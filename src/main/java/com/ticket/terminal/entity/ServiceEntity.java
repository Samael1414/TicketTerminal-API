package com.ticket.terminal.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private String description;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Long activeKindId;

    @Column(name = "is_need_visit_date")
    private Boolean needVisitDate;

    @Column(name = "is_need_visit_time")
    private Boolean needVisitTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dtBegin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dtEnd;

    private Integer proCultureIdentifier;

    @Column(name = "is_pro_culture_checked")
    private Boolean proCultureChecked;

    @Column(name = "is_disable_edit_visit_object", nullable = false)
    private Boolean disableEditVisitObject;

    @Column(name = "is_disable_edit_visitor", nullable = false)
    private Boolean disableEditVisitor;

    @Column(name = "is_visit_object_use_for_cost", nullable = false)
    private Boolean visitObjectUseForCost;

    @Column(name = "is_category_visitor_use_for_cost", nullable = false)
    private Boolean categoryVisitorUseForCost;

    @Column(name = "is_visitor_count_use_for_cost", nullable = false)
    private Boolean visitorCountUseForCost;

    @Column(name = "is_use_one_category", nullable = false)
    private Boolean useOneCategory;
}
