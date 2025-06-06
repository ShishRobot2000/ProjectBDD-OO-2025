package dao;

import model.Utente;
import interfacceDAO.IUtenteDAO;
import database.ConnessioneDatabase;

import java.sql.*;

public class UtenteDAO implements IUtenteDAO {

    @Override
    public boolean salvaUtente(Utente utente) {
        String sql = "INSERT INTO utente (username, password) VALUES (?, ?)";

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

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

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


