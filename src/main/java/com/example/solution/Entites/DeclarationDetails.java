package com.example.solution.Entites;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationDetails {
    @Id
    private String numDeclaration;
    private String sens;
    private String dateEnvoi;
    private String dateLivr;
    private String origine;
    private String destenation;
    private String colis;
    private String poids;
    private String volume;
    private String valeurDeclaree;
    private String cc;
    private String cr;
    private String ct;
    private String raisonSocialeDestinataire;
    private String retourBl;
    private String mntHt;
    private String numFacture;
    private String status;
    private String commentaireLivMobile;
    private String dateCommentaireLivMobile;
    private String nbrLad;
}
