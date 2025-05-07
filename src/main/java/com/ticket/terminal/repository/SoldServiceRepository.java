package com.ticket.terminal.repository;

import com.ticket.terminal.entity.SoldServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoldServiceRepository extends JpaRepository<SoldServiceEntity, Long> {

    @Modifying
    @Query("DELETE FROM SoldServiceEntity s WHERE s.orderServiceId IN :orderServiceIds")
    void deleteByOrderServiceIds(@Param("orderServiceIds") List<Long> orderServiceIds);

    @Modifying
    @Query("""
        UPDATE SoldServiceEntity s
        SET s.serviceStateId = 2
        WHERE s.orderServiceId IN :ids
    """)
    int markAsRefunded(@Param("ids") List<Long> orderServiceIds);


    Optional<SoldServiceEntity> findByOrderServiceId(Long orderServiceId);

    @Query("""
    SELECT s FROM SoldServiceEntity s
    LEFT JOIN FETCH s.visitObject
    LEFT JOIN FETCH s.orderService os
    LEFT JOIN FETCH os.service
    WHERE s.orderServiceId IN :orderServiceIds
""")
    List<SoldServiceEntity> findAllWithVisitObjects(@Param("orderServiceIds") List<Long> orderServiceIds);




}
