package com.example.solution.security.repo;

import com.example.solution.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface AppUserRepository extends JpaRepository<AppUser,String>{
    AppUser findByUsername(String username);
    @Query("SELECT u.userId FROM AppUser u WHERE u.username = :username")
    String findUserIdByUsername(String username);
    @Query("SELECT u.client.cpk FROM AppUser u WHERE u.username = :username")
    String findCpkClientByUsername(@Param("username") String username);
}

