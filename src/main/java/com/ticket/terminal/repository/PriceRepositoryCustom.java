package com.ticket.terminal.repository;

import com.ticket.terminal.entity.PriceEntity;
import java.util.Optional;

public interface PriceRepositoryCustom {

    Optional<PriceEntity> findMatchingPrice(Long serviceId, Long visitObject, Long categoryVisitorId);
}
