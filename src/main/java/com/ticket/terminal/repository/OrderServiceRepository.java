package com.ticket.terminal.repository;

import com.ticket.terminal.entity.OrderServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderServiceEntity, Long> {

    List<OrderServiceEntity> findAllByOrderId(Long orderId);

}
