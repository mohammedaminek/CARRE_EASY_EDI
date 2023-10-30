package com.example.solution.repositories;

import com.example.solution.Entites.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
    // Define custom queries or use default CRUD methods if needed
    @Query("SELECT c.numDebut FROM Client c WHERE c.cpk = :cpk")
    String findNumDebutByCpk(@Param("cpk")String cpk);
    @Query("SELECT c.carctereFin FROM Client c WHERE c.cpk = :cpk")
    String findcarctereFinByCpk(@Param("cpk")String cpk);

}