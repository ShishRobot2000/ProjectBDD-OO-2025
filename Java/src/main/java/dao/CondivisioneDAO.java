package dao;

import interfacceDAO.ICondivisioneDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
