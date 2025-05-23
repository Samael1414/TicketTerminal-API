package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ActionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {

    @Modifying
    @Query("DELETE FROM ActionLogEntity a WHERE a.createdAt < :threshold")
    void deleteOlderThan(LocalDateTime threshold);
}
