package com.ticket.terminal.repository.order;

import com.ticket.terminal.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
    SELECT o FROM OrderEntity o
    WHERE o.created BETWEEN :start AND :end
""")
    @EntityGraph(attributePaths = {"service", "service.service"})
    List<OrderEntity> findOrdersCreatedBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE OrderEntity o SET o.orderStateId = :stateId WHERE o.id = :orderId")
    void updateOrderState(@Param("orderId") Long orderId, @Param("stateId") Integer stateId);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {
            "service",
            "service.service"
    })
    Optional<OrderEntity> findById(@NonNull Long id);

    boolean existsByOrderBarcode(String orderBarcode);

    @Query("SELECT COALESCE(MAX(o.orderId), 0) FROM OrderEntity o")
    Long findMaxOrderId();

}
