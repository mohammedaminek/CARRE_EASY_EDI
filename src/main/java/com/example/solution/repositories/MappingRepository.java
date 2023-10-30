package com.example.solution.repositories;

import com.example.solution.Entites.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long> {
    @Query("SELECT m.CodeDestCarre FROM Mapping m WHERE m.codeDest = :codeDest")
    String findCodeDestCarreByCodeDestDestLike(@Param("codeDest")String codeDest);}