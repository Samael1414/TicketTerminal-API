package com.ticket.terminal.repository;

import com.ticket.terminal.entity.OrderServiceVisitorEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderServiceVisitorRepository extends JpaRepository<OrderServiceVisitorEntity, Long> {

    @Modifying
    @Query("DELETE FROM OrderServiceVisitorEntity v WHERE v.orderService.id IN :ids")
    void deleteByOrderServiceIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT v FROM OrderServiceVisitorEntity v WHERE v.orderService.id = :orderServiceId")
    List<OrderServiceVisitorEntity> findByOrderServiceId(@Param("orderServiceId") Long orderServiceId);


    @Modifying
    @Query(value = "INSERT INTO order_service_visitor (order_service_id, category_visitor_id, visitor_count) VALUES (:orderServiceId, :categoryVisitorId, :visitorCount)", nativeQuery = true)
    void insertVisitor(@Param("orderServiceId") Long orderServiceId,
                       @Param("categoryVisitorId") Long categoryVisitorId,
                       @Param("visitorCount") Long visitorCount);

}
