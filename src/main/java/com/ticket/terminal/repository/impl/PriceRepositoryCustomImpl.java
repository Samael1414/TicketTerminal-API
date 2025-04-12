package com.ticket.terminal.repository.impl;

import com.ticket.terminal.entity.PriceEntity;
import com.ticket.terminal.repository.PriceRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PriceRepositoryCustomImpl implements PriceRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Optional<PriceEntity> findMatchingPrice(Long serviceId, Long visitObject, Long categoryVisitorId) {
        String jpql = """
                SELECT p FROM PriceEntity p
                WHERE p.service.id = :serviceId
                  AND(p.visitObject.id = :visitObjectId OR (p.visitObject IS NULL AND :visitObjectId IS NULL))
                  AND(p.categoryVisitor.id = :categoryVisitorId OR (p.categoryVisitor IS NULL AND :categoryVisitorId IS NULL))
                ORDER BY
                  CASE WHEN p.visitObject IS NULL THEN 1 ELSE 0 END,
                  CASE WHEN p.categoryVisitor IS NULL THEN 1 ELSE 0 END
                """;

        List<PriceEntity> result = entityManager.createQuery(jpql, PriceEntity.class)
                .setParameter("serviceId", serviceId)
                .setParameter("visitObjectId", visitObject)
                .setParameter("categoryVisitorId", categoryVisitorId)
                .setMaxResults(1)
                .getResultList();

        return result.stream().findFirst();
    }
}
