package com.example.solution.repositories;

import com.example.solution.Entites.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnifiedRepository extends JpaRepository <Command,String> {

}