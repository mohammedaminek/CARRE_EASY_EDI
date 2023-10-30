package com.example.solution.Entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data // Combines @Getter, @Setter, @EqualsAndHashCode, and @ToString
@NoArgsConstructor // Generate a no-argument constructor
@AllArgsConstructor // Generate an all-argument constructor
public class Mapping {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long cpkDestination;
    private String codeDest; // Code for the destination for the client
    private String libelleDest; //  label for the destination
    private String CodeDestCarre; // Equivalent code for your system
    @ManyToOne
    @JoinColumn(name = "fk_Client")
    private Client client;
    @OneToMany(mappedBy = "mapping", cascade = CascadeType.ALL)
    private List<Command> commands = new ArrayList<>();

    @Override
    public String toString() {
        return "Destination{" +
                "cpkDestination='" + cpkDestination + '\'' +
                ", codeDest='" + codeDest + '\'' +
                ", libelleDest='" + libelleDest + '\'' +
                ", CodeDestCarré='" + CodeDestCarre + '\'' +
                '}';
    }

}
