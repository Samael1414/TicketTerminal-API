package com.ticket.terminal.repository;

import com.ticket.terminal.entity.GateInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateInfoRepository extends JpaRepository<GateInfoEntity, Long> {
}
