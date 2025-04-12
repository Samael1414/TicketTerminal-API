package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ExcursionLogVisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionLogVisitorRepository extends JpaRepository<ExcursionLogVisitorEntity, Long> {
}
