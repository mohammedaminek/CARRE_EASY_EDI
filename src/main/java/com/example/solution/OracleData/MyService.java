package com.example.solution.OracleData;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyService {
    String jdbcUrl = "jdbc:oracle:thin:@41.142.247.7:1521:orcl";
    String username = "ERSL";
    String password = "ersl";
    public Map<String, String> DeclarationsResult(String numDeclaration) {
        Map<String, String> results = new HashMap<>();
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT EXP.numero3 num_declaration, EXP.sens sens, " +
                    "to_char(EXP.datecreation,'DD/MM/YYYY') AS date_envoi, " +
                    "to_char(EXP.d5,'DD/MM/YYYY') AS date_livr, " +
                    "ORIGINE.LIBELLE Origine, " +
                    "DESTENATION.LIBELLE Destenation, " +
                    "EXP.NOMBRECOLIE Colis, " +
                    "EXP.POIDS Poids, " +
                    "EXP.VOLUME VOLUME, " +
                    "EXP.VALEURS Valeur_Declaree, " +
                    "decode(EXP.TYPEEXPEDITION,'C',EXP.CONTREMONTANT,0) cc, " +
                    "decode(EXP.TYPEEXPEDITION,'N',EXP.CONTREMONTANT,0) cr, " +
                    "decode(EXP.TYPEEXPEDITION,'T',EXP.CONTREMONTANT,0) ct, " +
                    "destinataire.libelle raison_sociale_destinataire, " +
                    "EXP.NUMBL retour_bl, EXP.mntht, " +
                    "F_FACTURE(exp.cpk) num_facture, " +
                    "DECODE(EXP.status,'creer', 'En cours','cloture', 'Livrée' ) status, " +
                    "f_Commentaire_mobilenew_e(exp.cpk) commentaire_liv_mobile, " +
                    "f_Commentaire_mobilenew_date_e(exp.cpk) date_commentaire_liv_mobile, " +
                    "f_nbr_tentationLAD(EXP.cpk) nbrLAD " +
                    "FROM expedition exp, clients destinataire, clients expediteur, " +
                    "magasins origine, magasins destenation " +
                    "WHERE EXP.CFKCLIENTDEST = destinataire.cpk " +
                    "AND EXP.cfkclientexp = expediteur.cpk " +
                    "AND EXP.CFKMAGASINEXP = ORIGINE.CPK " +
                    "AND EXP.CFKMAGASINDEST = DESTENATION.CPK " +
                    "AND EXP.status IN ('creer', 'cloture') " +
                    "AND EXP.numero3 = '" +numDeclaration+ "'";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    results.put(columnName, columnValue);
                    System.out.println(columnName + ": " + columnValue);
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
   public List<Map<String, String>> GetDataFromOracle(ArrayList<String> Declaration) {
        List<Map<String, String>> resultsList = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            for (String numero3 : Declaration) {
                String sqlQuery = "SELECT EXP.numero3 num_declaration, EXP.sens sens, " +
                        "to_char(EXP.datecreation,'DD/MM/YYYY') AS date_envoi, " +
                        "to_char(EXP.d5,'DD/MM/YYYY') AS date_livr, " +
                        "ORIGINE.LIBELLE Origine, " +
                        "DESTENATION.LIBELLE Destenation, " +
                        "EXP.NOMBRECOLIE Colis, " +
                        "EXP.POIDS Poids, " +
                        "EXP.VOLUME VOLUME, " +
                        "EXP.VALEURS Valeur_Declaree, " +
                        "decode(EXP.TYPEEXPEDITION,'C',EXP.CONTREMONTANT,0) cc, " +
                        "decode(EXP.TYPEEXPEDITION,'N',EXP.CONTREMONTANT,0) cr, " +
                        "decode(EXP.TYPEEXPEDITION,'T',EXP.CONTREMONTANT,0) ct, " +
                        "destinataire.libelle raison_sociale_destinataire, " +
                        "EXP.NUMBL retour_bl, EXP.mntht, " +
                        "F_FACTURE(exp.cpk) num_facture, " +
                        "DECODE(EXP.status,'creer', 'En cours','cloture', 'Livrée' ) status, " +
                        "f_Commentaire_mobilenew_e(exp.cpk) commentaire_liv_mobile, " +
                        "f_Commentaire_mobilenew_date_e(exp.cpk) date_commentaire_liv_mobile, " +
                        "f_nbr_tentationLAD(EXP.cpk) nbrLAD " +
                        "FROM expedition exp, clients destinataire, clients expediteur, " +
                        "magasins origine, magasins destenation " +
                        "WHERE EXP.CFKCLIENTDEST = destinataire.cpk " +
                        "AND EXP.cfkclientexp = expediteur.cpk " +
                        "AND EXP.CFKMAGASINEXP = ORIGINE.CPK " +
                        "AND EXP.CFKMAGASINDEST = DESTENATION.CPK " +
                        "AND EXP.status IN ('creer', 'cloture') " +
                        "AND EXP.numero3 = '" +numero3+ "'";
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String, String> resultMap = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String columnValue = resultSet.getString(i);
                        resultMap.put(columnName, columnValue);
                    }
                    resultsList.add(resultMap);
                }
                resultSet.close();
            }
            statement.close();
            connection.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(resultsList);
        return resultsList;
    }
}
