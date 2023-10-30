package com.example.solution.Entites;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Combines @Getter, @Setter, @EqualsAndHashCode, and @ToString
@NoArgsConstructor // Generate a no-argument constructor
@AllArgsConstructor // Generate an all-argument constructor
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clientModelPk;
    private String fieldName;
    private String NumCell;
    private String fieldCarreName;
    @ManyToOne
    @JoinColumn(name = "client_cpk") // Cette annotation définit la clé étrangère dans la table ClientModel
    private Client client;


}
