package com.ticket.terminal.repository;

import com.ticket.terminal.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u WHERE u.userName = :userName")
    Optional<UsersEntity> findByUserNameIgnoreCase(@Param("userName") String userName);
    
    @Query("SELECT u FROM UsersEntity u WHERE u.email = :email AND u.id != :id")
    List<UsersEntity> findByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    @Query("SELECT u FROM UsersEntity u WHERE u.userName = :userName AND u.id != :id")
    List<UsersEntity> findByUserNameAndIdNot(@Param("userName") String userName, @Param("id") Long id);
    
    @Query("SELECT u FROM UsersEntity u WHERE u.email = :email")
    Optional<UsersEntity> findByEmail(@Param("email") String email);
}
