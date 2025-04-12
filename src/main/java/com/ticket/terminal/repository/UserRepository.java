package com.ticket.terminal.repository;

import com.ticket.terminal.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u WHERE u.userName = :userName")
    Optional<UsersEntity> findByUserNameIgnoreCase(@Param("userName") String userName);


}
