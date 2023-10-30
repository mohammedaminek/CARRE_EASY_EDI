package com.example.solution.repositories;

import com.example.solution.Entites.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    @Query("SELECT f FROM UploadedFile f JOIN f.user u WHERE u.username = :username")
    List<UploadedFile> findByUsername(String username);
}