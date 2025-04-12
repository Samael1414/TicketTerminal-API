package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ExcursionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionLogRepository extends JpaRepository<ExcursionLogEntity, Long> {
}
