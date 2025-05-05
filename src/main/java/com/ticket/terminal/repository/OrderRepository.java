package com.ticket.terminal.repository;

import com.ticket.terminal.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
    SELECT DISTINCT o FROM OrderEntity o
    JOIN o.service s
    WHERE s.dtVisit BETWEEN :start AND :end
""")
    List<OrderEntity> findOrdersByDtVisitBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);




    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE OrderEntity o SET o.orderStateId = :stateId WHERE o.id = :orderId")
    void updateOrderState(@Param("orderId") Long orderId, @Param("stateId") Integer stateId);

    @EntityGraph(attributePaths = {
            "service",              // List<OrderServiceEntity>
            "service.service"       // OrderServiceEntity.service (=> ServiceEntity)
    })
    Optional<OrderEntity> findById(Long id);

    boolean existsByOrderBarcode(String orderBarcode);

    @Query("SELECT COALESCE(MAX(o.orderId), 0) FROM OrderEntity o")
    Integer findMaxOrderId();

}
