package com.ticket.terminal.repository;

import com.ticket.terminal.entity.OrderServiceEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderServiceEntity, Long> {

    List<OrderServiceEntity> findAllByOrderId(Long orderId);

    @Query("""
SELECT os FROM OrderServiceEntity os
JOIN FETCH os.service
WHERE os.id = :id
""")
    Optional<OrderServiceEntity> findWithServiceById(@Param("id") Long id);


}
