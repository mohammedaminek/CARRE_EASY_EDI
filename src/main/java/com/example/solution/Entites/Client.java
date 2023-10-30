package com.example.solution.Entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    private String cpk;
    private String libelleClient;
    private String codeOrigin;
    private String carctereFin = "#";
    private String numDebut;
    private String fileType;

    // Define the one-to-many relationship with Command
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Command> commands = new ArrayList<>();

    public Client(String cpk, String libelleClient) {
        this.cpk = cpk;
        this.libelleClient = libelleClient;
    }

    public Client(String cpk, String libelleClient, String numDebut,String carctereFin) {
        this.cpk = cpk;
        this.libelleClient = libelleClient;
        this.numDebut = numDebut;
        this.carctereFin = carctereFin;
    }

}
