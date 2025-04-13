package com.ticket.terminal.repository;

import com.ticket.terminal.entity.CategoryVisitorEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryVisitorRepository extends JpaRepository<CategoryVisitorEntity, Long> {

    @Query("""
        SELECT cv FROM CategoryVisitorEntity cv
        JOIN OrderServiceVisitorEntity osv ON cv.id = osv.categoryVisitor.id
        WHERE osv.orderService.id = :serviceId
    """)
    List<CategoryVisitorEntity> findByServiceId(@Param("serviceId") Long serviceId);
}
