package com.example.solution.security.entities;

import com.example.solution.Entites.Client;
import com.example.solution.Entites.UploadedFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    private String  userId;
    @Column(unique= true)
    private String  username;
    private String  password;
    private String  email;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppRole> roles= new ArrayList<>();;
    @OneToOne
    @JoinColumn(name = "client_cpk", referencedColumnName = "cpk")
    private Client client;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UploadedFile> uploadedFiles;
}
