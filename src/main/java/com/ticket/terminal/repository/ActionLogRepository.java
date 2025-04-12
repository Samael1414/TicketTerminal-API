package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ActionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
}
