package com.ticket.terminal.repository;

import com.ticket.terminal.entity.SystemInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoRepository extends JpaRepository<SystemInfoEntity, Long> {
}
