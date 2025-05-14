package com.ticket.terminal.repository;

import com.ticket.terminal.entity.CategoryVisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryVisitorRepository extends JpaRepository<CategoryVisitorEntity, Long> {

}
