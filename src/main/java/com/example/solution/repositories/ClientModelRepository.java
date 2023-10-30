package com.example.solution.repositories;

import com.example.solution.Entites.ClientModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientModelRepository extends CrudRepository<ClientModel, Long> {
    // Custom query method to find a ClientModel by client_id
    List<ClientModel> findByClient_Cpk(String clientId);
}

