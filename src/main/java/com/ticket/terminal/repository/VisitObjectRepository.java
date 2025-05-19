package com.ticket.terminal.repository;

import com.ticket.terminal.entity.VisitObjectEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitObjectRepository extends JpaRepository<VisitObjectEntity, Long> {

    @Query(value = """
    SELECT vo.* FROM visit_objects vo
    JOIN order_service_visit_object osv ON osv.visit_object_id = vo.id
    JOIN order_services os ON osv.order_service_id = os.id
    WHERE os.service_id = :serviceId
""", nativeQuery = true)
    List<VisitObjectEntity> findByServiceId(@Param("serviceId") Long serviceId);

    @Query(value = """
SELECT vo.* FROM visit_objects vo
JOIN order_service_visit_object osv ON osv.visit_object_id = vo.id
WHERE osv.order_service_id = :orderServiceId
""", nativeQuery = true)
    List<VisitObjectEntity> findByOrderServiceId(@Param("orderServiceId") Long orderServiceId);

    @Modifying
    @Query("DELETE FROM VisitObjectEntity v WHERE v.service.id = :serviceId")
    void deleteAllByServiceId(@Param("serviceId") Long serviceId);

    List<VisitObjectEntity> findAllByServiceId(Long serviceId);

}
