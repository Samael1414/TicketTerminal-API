package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "sold_services")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_service_id", nullable = false)
    private Long orderServiceId;

    private String barcode;

    private Integer serviceStateId;

    private Integer paymentKindId;

    private LocalDateTime dtActive;

    @Column(nullable = false, name = "service_cost")
    private Double serviceCost;

    @Column(nullable = false, name = "service_count")
    private Integer serviceCount;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "sold_service_visit_object",
            joinColumns = @JoinColumn(name = "sold_service_id"),
            inverseJoinColumns = @JoinColumn(name = "visit_object_id")
    )
    private List<VisitObjectEntity> visitObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_service_id",
            insertable = false,
            updatable  = false
    )
    private OrderServiceEntity orderService;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;


}
