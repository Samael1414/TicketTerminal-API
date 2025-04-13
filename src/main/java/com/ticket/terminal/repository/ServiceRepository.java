package com.ticket.terminal.repository;

import com.ticket.terminal.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("SELECT s FROM ServiceEntity s WHERE s.disableEditVisitor = false AND s.disableEditVisitObject = false")
    List<ServiceEntity> findAllEditableServices();

}
