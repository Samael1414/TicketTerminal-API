package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {


    @Query(value = """
            SELECT s.*
            FROM   services s
            WHERE  EXISTS (
                   SELECT 1
                   FROM   prices p
                   WHERE  p.service_id = s.id
                     AND (p.visit_object_id    IS NOT NULL
                          OR p.category_visitor_id IS NOT NULL))
            """, nativeQuery = true)
    List<ServiceEntity> findAllEditableServices();


    @Query(value = """
            SELECT s.*
            FROM   services s
            WHERE  NOT EXISTS (
                   SELECT 1
                   FROM   prices p
                   WHERE  p.service_id = s.id
                     AND (p.visit_object_id    IS NOT NULL
                          OR p.category_visitor_id IS NOT NULL))
            """, nativeQuery = true)
    List<ServiceEntity> findAllSimpleServices();

}
