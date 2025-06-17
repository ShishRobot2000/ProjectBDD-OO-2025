package dao;

import interfacceDAO.ICondivisioneDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CondivisioneDAO implements ICondivisioneDAO {

    @Override
    public boolean condividi(String username, String prop, String tipo, String titolo) {
    String queryId = "SELECT id FROM todo WHERE proprietario = ? AND tipo_bacheca = ? AND titolo = ?";
    String insertCondivisione = "INSERT INTO condivisione (username_utente, id_todo, stato) VALUES (?, ?, 'PENDING')";

    try (Connection conn = ConnessioneDatabase.getConnection();
         PreparedStatement stmtSelect = conn.prepareStatement(queryId)) {

        stmtSelect.setString(1, prop);
        stmtSelect.setString(2, tipo);
        stmtSelect.setString(3, titolo);

        ResultSet rs = stmtSelect.executeQuery();
        if (rs.next()) {
            int idToDo = rs.getInt("id");

            try (PreparedStatement stmtInsert = conn.prepareStatement(insertCondivisione)) {
                stmtInsert.setString(1, username);
                stmtInsert.setInt(2, idToDo);
                return stmtInsert.executeUpdate() > 0;
            }

        } else {
            System.err.println("ToDo non trovato per prop=" + prop + ", tipo=" + tipo + ", titolo=" + titolo);
            return false;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    @Override
    public boolean rimuoviCondivisione(String username, String prop, String tipo, String titolo) {
        String sql = """
        DELETE FROM condivisione 
        WHERE username_utente = ? 
          AND id_todo = (
            SELECT id FROM todo WHERE proprietario = ? AND tipo_bacheca = ? AND titolo = ?
          )
    """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, prop);
            stmt.setString(3, tipo);
            stmt.setString(4, titolo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean esisteCondivisione(String username, String prop, String tipo, String titolo) {
        String sql = """
        SELECT 1 FROM condivisione 
        WHERE username_utente = ? 
          AND id_todo = (
            SELECT id FROM todo WHERE proprietario = ? AND tipo_bacheca = ? AND titolo = ?
          )
    """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, prop);
            stmt.setString(3, tipo);
            stmt.setString(4, titolo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String[]> getRichiestePendentiPerUtente(String usernameUtente) {
        List<String[]> richieste = new ArrayList<>();
        String sql = """
        SELECT t.proprietario AS richiedente, t.tipo_bacheca, t.titolo
        FROM condivisione c
        JOIN todo t ON c.id_todo = t.id
        WHERE c.username_utente = ? AND c.stato = 'PENDING'
       """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, usernameUtente);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String richiedente = rs.getString("richiedente");
            String tipoBacheca = rs.getString("tipo_bacheca");
            String titolo = rs.getString("titolo");
            richieste.add(new String[] { richiedente, tipoBacheca, titolo });
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return richieste;
    }

    public boolean aggiornaStatoRichiesta(String username, String proprietario, String tipo, String titolo, String nuovoStato) {
        System.out.println("[DEBUG] aggiornaStatoRichiesta params: username=" + username + ", proprietario=" + proprietario + ", tipo=" + tipo + ", titolo=" + titolo);

        String queryId = "SELECT id FROM todo WHERE proprietario = ? AND tipo_bacheca = ? AND titolo = ?";
        String updateSql = "UPDATE condivisione SET stato = ? WHERE username_utente = ? AND id_todo = ?";
        String deleteSql = "DELETE FROM condivisione WHERE username_utente = ? AND id_todo = ? AND stato = 'PENDING'";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmtId = conn.prepareStatement(queryId)) {

            stmtId.setString(1, proprietario);
            stmtId.setString(2, tipo);
            stmtId.setString(3, titolo);

            ResultSet rs = stmtId.executeQuery();
            if (rs.next()) {
                int idTodo = rs.getInt("id");

                if (nuovoStato.equalsIgnoreCase("ACCEPTED")) {
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(updateSql)) {
                        stmtUpdate.setString(1, nuovoStato);
                        stmtUpdate.setString(2, username);
                        stmtUpdate.setInt(3, idTodo);
                        return stmtUpdate.executeUpdate() > 0;
                    }
                } else if (nuovoStato.equalsIgnoreCase("REJECTED")) {
                    try (PreparedStatement stmtDelete = conn.prepareStatement(deleteSql)) {
                        stmtDelete.setString(1, username);
                        stmtDelete.setInt(2, idTodo);
                        return stmtDelete.executeUpdate() > 0;
                    }
                } else {
                    System.err.println("[ERRORE] Stato non valido: " + nuovoStato);
                    return false;
                }

            } else {
                System.err.println("ToDo non trovato per aggiornamento stato richiesta");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    
    public boolean rimuoviRichiesta(String username, String proprietario, String tipo, String titolo) {
    String queryId = "SELECT id FROM todo WHERE proprietario = ? AND tipo_bacheca = ? AND titolo = ?";
    String deleteSql = "DELETE FROM condivisione WHERE username_utente = ? AND id_todo = ? AND stato = 'PENDING'";
    
    try (Connection conn = ConnessioneDatabase.getConnection();
         PreparedStatement stmtId = conn.prepareStatement(queryId)) {
         
        stmtId.setString(1, proprietario);
        stmtId.setString(2, tipo);
        stmtId.setString(3, titolo);
        
        ResultSet rs = stmtId.executeQuery();
        if (rs.next()) {
            int idTodo = rs.getInt("id");
            
            try (PreparedStatement stmtDelete = conn.prepareStatement(deleteSql)) {
                stmtDelete.setString(1, username);
                stmtDelete.setInt(2, idTodo);
                
                return stmtDelete.executeUpdate() > 0;
            }
        } else {
            System.err.println("ToDo non trovato per rimuovi richiesta");
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean eliminaCondivisioniCollegate(int idToDo) {
        String sql = "DELETE FROM condivisione WHERE id_todo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idToDo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
