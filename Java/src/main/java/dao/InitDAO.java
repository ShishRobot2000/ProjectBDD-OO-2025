package dao;

import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class InitDAO {

    public static void inserisciTipiEStati() {
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                INSERT INTO tipobacheca (nome) VALUES 
                ('Universita'), 
                ('Lavoro'), 
                ('TempoLibero')
                ON CONFLICT DO NOTHING
            """);

            stmt.executeUpdate("""
                INSERT INTO statotodo (nome) VALUES 
                ('Completato'), 
                ('NonCompletato')
                ON CONFLICT DO NOTHING
            """);

        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione:");
            e.printStackTrace();
        }
    }
}


