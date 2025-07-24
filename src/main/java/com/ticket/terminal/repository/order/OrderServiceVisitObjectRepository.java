package com.ticket.terminal.repository.order;

import com.ticket.terminal.entity.order.OrderServiceVisitObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderServiceVisitObjectRepository extends JpaRepository<OrderServiceVisitObjectEntity, Long> {

    void deleteByOrderServiceIdIn(List<Long> orderServiceIds);
}
