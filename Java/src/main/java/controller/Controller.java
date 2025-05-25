package controller;

import gui.*;
import model.*;
import service.*;

import javax.swing.*;

public class Controller {

    private final BoardPanel universitaBoard;
    private final BoardPanel lavoroBoard;
    private final BoardPanel tempoLiberoBoard;

    public Controller(BoardPanel universitaBoard, BoardPanel lavoroBoard, BoardPanel tempoLiberoBoard) {
        this.universitaBoard = universitaBoard;
        this.lavoroBoard = lavoroBoard;
        this.tempoLiberoBoard = tempoLiberoBoard;
    }

    public void addNewToDo(BoardPanel board) {
        Bacheca bacheca = board.getBacheca();

        ToDo nuovoToDo = new ToDo("", "", null, null, "", "FFFFFF", "");

        ToDoFormDialog dialog = new ToDoFormDialog(nuovoToDo, true);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(board);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            if (!nuovoToDo.getTitolo().isEmpty()) {
                bacheca.getToDoList().add(nuovoToDo);
                board.addToDo(nuovoToDo);
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
            if (bacheca != null) {
                bacheca.getToDoList().remove(todo);
            }

            board.clearToDos();
            for (ToDo t : bacheca.getToDoList()) {
                board.addToDo(t);
            }
        }
    }

    public void loadUser(String username) {
        Utente utente = AuthService.getUtente(username);

        universitaBoard.setBacheca(null);
        lavoroBoard.setBacheca(null);
        tempoLiberoBoard.setBacheca(null);

        for (Bacheca b : utente.getBacheche()) {
            BoardPanel target = switch (b.getTipo()) {
                case Università -> universitaBoard;
                case Lavoro -> lavoroBoard;
                case TempoLibero -> tempoLiberoBoard;
            };

            target.setBacheca(b);
        }
    }

    public boolean login(String username, String password, MainFrame parent) {
        // Sostituire con un servizio AuthService reale se disponibile
        if (username.equals("admin") && password.equals("1234")) {
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
            updateView.run(); // Chiamato dalla GUI per aggiornare il pannello
        }
    }

}

