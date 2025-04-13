package com.ticket.terminal.repository;

import com.ticket.terminal.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long>, PriceRepositoryCustom {

    List<PriceEntity> findAllByServiceId(Long serviceId);
}
