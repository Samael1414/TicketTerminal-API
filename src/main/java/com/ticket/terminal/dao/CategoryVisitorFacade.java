package com.ticket.terminal.dao;

import com.ticket.terminal.dto.CategoryVisitorDto;

import java.util.List;

public interface CategoryVisitorFacade {

    CategoryVisitorDto findById(long id);
    List<CategoryVisitorDto> findAll();
    CategoryVisitorDto save(CategoryVisitorDto categoryVisitorDto);
    void delete(CategoryVisitorDto categoryVisitorDto);
}
