package com.ticket.terminal.repository.excursion;

import com.ticket.terminal.entity.excursion.ExcursionLogVisitObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionLogVisitObjectRepository extends JpaRepository<ExcursionLogVisitObjectEntity, Long> {
}
