package com.ticket.terminal.repository;

import com.ticket.terminal.entity.OrderServiceVisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderServiceVisitorRepository extends JpaRepository<OrderServiceVisitorEntity, Long> {
    void deleteByOrderServiceIdIn(List<Long> orderServiceIds);
}
