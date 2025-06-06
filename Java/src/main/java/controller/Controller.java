package controller;

import gui.*;
import model.*;
import dao.*;
import interfacceDAO.*;

import javax.swing.*;
import java.util.List;

public class Controller {

    private final BoardPanel universitaBoard;
    private final BoardPanel lavoroBoard;
    private final BoardPanel tempoLiberoBoard;

    private final IUtenteDAO utenteDAO;
    private final IBachecaDAO bachecaDAO;
    private final IToDoDAO toDoDAO;

    private Utente utenteCorrente;

    public Controller(BoardPanel universitaBoard, BoardPanel lavoroBoard, BoardPanel tempoLiberoBoard) {
        this.universitaBoard = universitaBoard;
        this.lavoroBoard = lavoroBoard;
        this.tempoLiberoBoard = tempoLiberoBoard;

        this.utenteDAO = new UtenteDAO();
        this.bachecaDAO = new BachecaDAO();
        this.toDoDAO = new ToDoDAO();
    }

    public void addNewToDo(BoardPanel board) {
        Bacheca bacheca = board.getBacheca();

        ToDo nuovoToDo = new ToDo("", "", "", "", "", "FFFFFF");

        ToDoFormDialog dialog = new ToDoFormDialog(nuovoToDo, true);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(board);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            if (!nuovoToDo.getTitolo().isEmpty()) {
                // Salva nel database: posizione gestita internamente dal DAO
                toDoDAO.salva(nuovoToDo, utenteCorrente.getUsername(), bacheca.getTipo());

                // Ricarica ToDo aggiornati
                board.clearToDos();
                List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), bacheca.getTipo());
                bacheca.setToDoList(todos);
                todos.forEach(board::addToDo);
            } else {
                JOptionPane.showMessageDialog(board, "Il titolo non può essere vuoto.");
            }
        }
    }

    public void removeToDo(BoardPanel board, ToDo todo) {
        Bacheca bacheca = board.getBacheca();

        int option = JOptionPane.showConfirmDialog(board,
                "Sei sicuro di voler rimuovere questo ToDo?",
                "Conferma Rimozione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            toDoDAO.elimina(todo.getTitolo(), utenteCorrente.getUsername(), bacheca.getTipo());

            board.clearToDos();
            List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), bacheca.getTipo());
            bacheca.setToDoList(todos);
            todos.forEach(board::addToDo);
        }
    }

    public void loadUser(String username) {
        utenteCorrente = utenteDAO.findByUsername(username);
        if (utenteCorrente == null) {
            JOptionPane.showMessageDialog(null, "Utente non trovato.");
            return;
        }

        universitaBoard.setBacheca(null);
        lavoroBoard.setBacheca(null);
        tempoLiberoBoard.setBacheca(null);

        for (TipoBacheca tipo : TipoBacheca.values()) {
            Bacheca b = bachecaDAO.findByTipo(username, tipo);
            if (b == null) continue;

            List<ToDo> todos = toDoDAO.trovaPerBacheca(username, tipo);
            b.setToDoList(todos);

            BoardPanel target = switch (tipo) {
                case Università -> universitaBoard;
                case Lavoro -> lavoroBoard;
                case TempoLibero -> tempoLiberoBoard;
            };

            target.setBacheca(b);
            todos.forEach(target::addToDo);
        }
    }

    public boolean login(String username, String password, MainFrame parent) {
        Utente utente = utenteDAO.findByUsernameAndPassword(username, password);
        if (utente != null) {
            utenteCorrente = utente;
            parent.showDashboard(username);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Credenziali errate");
            return false;
        }
    }

    public void editToDo(BoardPanel board, ToDo todo, boolean isEditable, Runnable updateView) {
        ToDoFormDialog dialog = new ToDoFormDialog(todo, isEditable);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(board);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Bacheca bacheca = board.getBacheca();
            toDoDAO.aggiorna(todo, utenteCorrente.getUsername(), bacheca.getTipo());

            updateView.run(); // Aggiorna la card
        }
    }

    public boolean register(String username, String password) {

       System.out.println("[DEBUG] Provo a registrare: '" + username + "'");

       username = username.trim();

       if (utenteDAO.findByUsername(username) != null) {
          System.out.println("[DEBUG] Registrazione fallita: utente esiste già");
          return false;
      } else {
          System.out.println("[DEBUG] Utente nuovo: procedo alla registrazione");
          Utente nuovoUtente = new Utente(username, password);
          boolean result = utenteDAO.salvaUtente(nuovoUtente);
          System.out.println("[DEBUG] Salvataggio completato? " + result);
          return result;
     }
    }
    
    // Carica l'utente e lo memorizza 
    public Utente getUtenteCorrente() {
       return utenteCorrente;
  }
}

