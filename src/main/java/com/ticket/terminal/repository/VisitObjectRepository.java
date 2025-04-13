package com.ticket.terminal.repository;

import com.ticket.terminal.entity.VisitObjectEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitObjectRepository extends JpaRepository<VisitObjectEntity, Long> {

    @Query("""
            SELECT v FROM VisitObjectEntity v JOIN OrderServiceVisitObjectEntity osv ON osv.visitObject.id = v.id
            WHERE osv.orderService.service.id = :serviceId
            """)
    List<VisitObjectEntity> findByServiceId(@Param("serviceId") Long serviceId);
}
