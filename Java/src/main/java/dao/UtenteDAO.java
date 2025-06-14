package dao;

import model.Utente;
import interfacceDAO.IUtenteDAO;
import database.ConnessioneDatabase;

import java.sql.*;

public class UtenteDAO implements IUtenteDAO {

    @Override
    public boolean salvaUtente(Utente utente) {
        String sql = "INSERT INTO utente (username, password) VALUES (?, ?)";

        utente.setUsername(utente.getUsername().trim().toLowerCase());

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getPassword());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Utente findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM utente WHERE username = ? AND password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utente(rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utente findByUsername(String username) {
        String sql = "SELECT * FROM utente WHERE username = ?";

        System.out.println("[DEBUG] Cerco utente: " + username + "'");

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());
            

            ResultSet rs = stmt.executeQuery();
            
            
            if (rs.next()) {
                String foundUser = rs.getString("username");
                System.out.println("[DEBUG] Utente trovato: " + foundUser);
                return new Utente(foundUser, rs.getString("password"));
            } else {
                System.out.println("[DEBUG] Nessun utente trovato.");
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean eliminaUtente(String username) {
        String sql = "DELETE FROM utente WHERE username = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


