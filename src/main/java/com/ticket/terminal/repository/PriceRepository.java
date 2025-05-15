package com.ticket.terminal.repository;

import com.ticket.terminal.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long>, PriceRepositoryCustom {

    List<PriceEntity> findAllByServiceId(Long serviceId);

    @Modifying
    @Query("DELETE FROM PriceEntity p WHERE p.service.id = :serviceId")
    void deleteAllByServiceId(@Param("serviceId") Long serviceId);
}
