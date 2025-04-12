package com.ticket.terminal.dao;

import com.ticket.terminal.dto.UsersResponseDto;

import java.util.List;

public interface UserFacade {

    UsersResponseDto findById(Long id);
    List<UsersResponseDto> findAll();
    UsersResponseDto save(UsersResponseDto usersResponseDto);
    void delete(UsersResponseDto usersResponseDto);
}
