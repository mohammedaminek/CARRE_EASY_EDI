    package com.example.solution.Entites;

    import com.example.solution.security.entities.AppUser;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Command {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idCommand;
        private String ramasseur = "0";
        private String declaration = "0";
        private String dest_code = "0";
        private String dest_libelle = "-";
        private String adresse = "-";
        private String telephone = "0000000000";
        private String nature = "s";
        private String fond = "0";
        private String origin = "000";
        private String destination = "0";
        private String colis = "0";
        private String poids = "0";
        private String volume = "0";
        private String valeur_dec = "0";
        private String livraison = "D";
        private String port = "PPE";
        private String bl = "0";
        private String num_bl = "0";
        private String facture = "0";
        private String num_facture = "0";
        private String catProduit = "DEFAULT";
        private String ps = "0";
        private String camion = "";
        private String numero = "0";

        @ManyToOne
        @JoinColumn(name = "client_cpk")
        private Client client;
        @ManyToOne
        @JoinColumn(name = "user_id")
        private AppUser user;
        @ManyToOne
        @JoinColumn(name = "fk_Mapping")
        private Mapping mapping;
    }
