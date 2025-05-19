package com.ticket.terminal.repository;

import com.ticket.terminal.entity.CategoryVisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryVisitorRepository extends JpaRepository<CategoryVisitorEntity, Long> {

    List<CategoryVisitorEntity> findAllByIdIn(Set<Long> ids);

}
