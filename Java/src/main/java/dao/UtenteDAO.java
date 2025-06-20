package dao;

import model.Utente;
import interfacceDAO.IUtenteDAO;
import database.ConnessioneDatabase;

import java.sql.*;

/**
 * Implementazione dell'interfaccia {@link IUtenteDAO} per la gestione della tabella "utente".
 * Esegue le operazioni CRUD di base sul database PostgreSQL.
 */
public class UtenteDAO implements IUtenteDAO {

    /**
     * Salva un nuovo utente nel database.
     *
     * @param utente L'oggetto {@link Utente} da inserire
     * @return true se l'inserimento ha avuto successo, false altrimenti
     */
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

    /**
     * Cerca un utente nel database usando username e password.
     *
     * @param username L'username dell'utente
     * @param password La password dell'utente
     * @return L'oggetto {@link Utente} trovato o null se non esiste
     */
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

    /**
     * Cerca un utente nel database solo tramite username.
     *
     * @param username L'username da cercare
     * @return L'oggetto {@link Utente} trovato o null se non esiste
     */
    @Override
    public Utente findByUsername(String username) {
        String sql = "SELECT * FROM utente WHERE username = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utente(rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina un utente dal database in base allo username.
     *
     * @param username Lo username dell'utente da eliminare
     * @return true se l'eliminazione ha avuto successo, false altrimenti
     */
    @Override
    public boolean eliminaUtente(String username) {
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            conn.setAutoCommit(false); // Inizio transazione

            // 1. Elimina condivisioni ricevute
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM condivisione WHERE destinatario = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // 2. Elimina condivisioni fatte
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM condivisione WHERE proprietario = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // 3. Elimina todo dell'utente
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM todo WHERE proprietario = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // 4. Elimina bacheche dell'utente
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM bacheca WHERE proprietario = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // 5. Elimina l’utente
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM utente WHERE username = ?")) {
                stmt.setString(1, username);
                boolean result = stmt.executeUpdate() > 0;
                conn.commit(); // Commit transazione
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ConnessioneDatabase.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }


    /**
     * Verifica se un utente esiste già nel database.
     *
     * @param username Lo username da verificare
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean esisteUtente(String username) {
        String sql = "SELECT 1 FROM utente WHERE username = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true se almeno una riga è presente
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}



