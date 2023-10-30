package com.example.solution.Service;

import com.example.solution.repositories.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MappingService {

    private final MappingRepository mappingRepository;

    @Autowired
    public MappingService(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }


    // Add other business logic and methods related to Destination here
}