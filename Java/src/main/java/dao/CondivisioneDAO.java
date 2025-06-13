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
        String sql = "INSERT INTO condivisione VALUES (?, ?, ?, ?)";
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
    public boolean rimuoviCondivisione(String username, String prop, String tipo, String titolo) {
        String sql = "DELETE FROM condivisione WHERE username_utente = ? AND proprietario_todo = ? AND tipo_bacheca_todo = ? AND titolo_todo = ?";
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
        String sql = "SELECT 1 FROM condivisione WHERE username_utente = ? AND proprietario_todo = ? AND tipo_bacheca_todo = ? AND titolo_todo = ?";
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

    public List<String[]> getRichiestePendentiPerUtente(String proprietario) {
        List<String[]> richieste = new ArrayList<>();
        String sql = """
            SELECT c.username_utente, t.tipo_bacheca, t.titolo
            FROM condivisione c
            JOIN todo t ON c.id_todo = t.id
            WHERE t.proprietario = ? AND t.stato = 'PENDING'
            """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, proprietario);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String richiedente = rs.getString("username_utente");
            String tipoBacheca = rs.getString("tipo_bacheca");
            String titolo = rs.getString("titolo_todo");
            richieste.add(new String[] { richiedente, tipoBacheca, titolo });
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return richieste;
    }

    public boolean aggiornaStatoRichiesta(String username, String proprietario, String tipo, String titolo, String nuovoStato) {
        String sql = "UPDATE condivisione SET stato = ? WHERE username_utente = ? AND proprietario_todo = ? AND tipo_bacheca_todo = ? AND titolo_todo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setString(2, username);
            stmt.setString(3, proprietario);
            stmt.setString(4, tipo);
            stmt.setString(5, titolo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean rimuoviRichiesta(String username, String proprietario, String tipo, String titolo) {
    String sql = "DELETE FROM condivisione WHERE username_utente = ? AND proprietario_todo = ? AND tipo_bacheca_todo = ? AND titolo_todo = ? AND stato = 'PENDING'";
    try (Connection conn = ConnessioneDatabase.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, proprietario);
        stmt.setString(3, tipo);
        stmt.setString(4, titolo);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
  }


}
