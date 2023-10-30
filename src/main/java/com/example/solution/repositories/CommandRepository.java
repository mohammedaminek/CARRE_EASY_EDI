package com.example.solution.repositories;

import com.example.solution.Entites.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface CommandRepository extends JpaRepository<Command,Long>{
    Page<Command> findByDeclarationContains(String keyword, Pageable pageable);
    @Query("SELECT c FROM Command c INNER JOIN c.user u WHERE u.username = :username AND c.declaration LIKE %:keyword%")
    Page<Command> findByUsernameAndDeclarationContains(String username, String keyword, Pageable pageable);
    @Query("SELECT c.declaration FROM Command c WHERE c.user.userId = :userId")
    ArrayList<String> findDeclarationsByUserId(String userId);
}

