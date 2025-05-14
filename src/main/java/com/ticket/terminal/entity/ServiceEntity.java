package com.ticket.terminal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

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
    private Double cost;

    @Column(nullable = false)
    private Long activeKindId;

    @Column(name = "is_need_visit_date")
    private Boolean isNeedVisitDate;

    @Column(name = "is_need_visit_time")
    private Boolean isNeedVisitTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime dtBegin;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime dtEnd;

    private Integer proCultureIdentifier;

    @Column(name = "is_pro_culture_checked")
    private Boolean isPROCultureChecked;

    @Column(name = "is_disable_edit_visit_object", nullable = false)
    private Boolean IsDisableEditVisitObject;

    @Column(name = "is_disable_edit_visitor", nullable = false)
    private Boolean isDisableEditVisitor;

    @Column(name = "is_visit_object_use_for_cost", nullable = false)
    private Boolean isVisitObjectUseForCost;

    @Column(name = "is_category_visitor_use_for_cost", nullable = false)
    private Boolean isCategoryVisitorUseForCost;

    @Column(name = "is_visitor_count_use_for_cost", nullable = false)
    private Boolean isVisitorCountUseForCost;

    @Column(name = "is_use_one_category", nullable = false)
    private Boolean useOneCategory;
}
