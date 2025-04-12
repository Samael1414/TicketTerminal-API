package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ProCultureLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProCultureLinkRepository extends JpaRepository<ProCultureLinkEntity, Long> {
}
