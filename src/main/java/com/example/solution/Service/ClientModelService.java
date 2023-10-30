package com.example.solution.Service;

import com.example.solution.Entites.ClientModel;
import com.example.solution.repositories.ClientModelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClientModelService {
    private final ClientModelRepository clientModelRepository;

    @Autowired
    public ClientModelService(ClientModelRepository clientModelRepository) {
        this.clientModelRepository = clientModelRepository;
    }


    public ClientModel getClientModelById(Long submissionKey) {
        return clientModelRepository.findById(submissionKey).orElse(null);
    }

    // Other CRUD methods as needed

    private String convertFormDataToJson(Map<String, String> formData) {
        // Convert formData to JSON (using Jackson library)
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(formData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }
}
