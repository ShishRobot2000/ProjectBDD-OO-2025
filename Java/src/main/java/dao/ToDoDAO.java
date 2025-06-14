package dao;

import model.ToDo;
import model.StatoToDo;
import model.TipoBacheca;
import interfacceDAO.IToDoDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoDAO implements IToDoDAO {

    @Override
    public boolean salva(ToDo todo, String proprietario, TipoBacheca tipoBacheca) {
        String aggiornaPosizioni = "UPDATE todo " +
                "SET posizione = posizione + 1 " +
                "WHERE proprietario = ? AND tipo_bacheca = ?";

        String insert = "INSERT INTO todo (titolo, descrizione, data_scadenza, colore, stato, url, immagine, posizione, proprietario, tipo_bacheca) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUpdate = conn.prepareStatement(aggiornaPosizioni)) {
                stmtUpdate.setString(1, proprietario);
                stmtUpdate.setString(2, tipoBacheca.name());
                stmtUpdate.executeUpdate();
            }

            try (PreparedStatement stmtInsert = conn.prepareStatement(insert)) {
                stmtInsert.setString(1, todo.getTitolo());
                stmtInsert.setString(2, todo.getDescrizione());
                stmtInsert.setString(3, todo.getDataDiScadenza());
                stmtInsert.setString(4, todo.getColore());
                stmtInsert.setString(5, todo.getStato().name());
                stmtInsert.setString(6, todo.getUrl());
                stmtInsert.setString(7, todo.getImmagine());
                stmtInsert.setInt(8, 1);
                stmtInsert.setString(9, proprietario);
                stmtInsert.setString(10, tipoBacheca.name());

                ResultSet rs = stmtInsert.executeQuery();
                if (rs.next()) {
                    todo.setId(rs.getInt("id")); // assegna l'ID generato
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ToDo> trovaPerBacheca(String proprietario, TipoBacheca tipoBacheca) {
        List<ToDo> lista = new ArrayList<>();
        String sql = "SELECT * FROM todo WHERE proprietario = ? AND tipo_bacheca = ? ORDER BY posizione ASC";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proprietario);
            stmt.setString(2, tipoBacheca.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ToDo todo = new ToDo(
                        rs.getString("titolo"),
                        rs.getString("data_scadenza"),
                        rs.getString("url"),
                        rs.getString("immagine"),
                        rs.getString("descrizione"),
                        rs.getString("colore")
                );
                todo.setId(rs.getInt("id"));
                todo.setStato(StatoToDo.valueOf(rs.getString("stato")));
                todo.setPosizione(rs.getInt("posizione"));

                lista.add(todo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public boolean aggiorna(ToDo todo, String proprietario, TipoBacheca tipoBacheca) {
        String sql = "UPDATE todo SET titolo = ?, descrizione = ?, data_scadenza = ?, colore = ?, stato = ?, " +
                "url = ?, immagine = ?, posizione = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            stmt.setString(3, todo.getDataDiScadenza());
            stmt.setString(4, todo.getColore());
            stmt.setString(5, todo.getStato().name());
            stmt.setString(6, todo.getUrl());
            stmt.setString(7, todo.getImmagine());
            stmt.setInt(8, todo.getPosizione());
            stmt.setInt(9, todo.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM todo WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}



