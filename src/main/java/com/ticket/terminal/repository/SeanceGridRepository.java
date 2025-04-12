package com.ticket.terminal.repository;

import com.ticket.terminal.entity.SeanceGridEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceGridRepository extends JpaRepository<SeanceGridEntity, Long> {
}
