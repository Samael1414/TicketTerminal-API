package com.ticket.terminal.repository;

import com.ticket.terminal.entity.RequisiteInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisiteInfoRepository extends JpaRepository<RequisiteInfoEntity, Long> {
}
