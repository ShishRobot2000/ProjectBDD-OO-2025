package controller;

import gui.*;
import model.*;
import dao.*;
import interfacceDAO.*;

import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private final BoardPanel universitaBoard;
    private final BoardPanel lavoroBoard;
    private final BoardPanel tempoLiberoBoard;

    private final IUtenteDAO utenteDAO;
    private final IBachecaDAO bachecaDAO;
    private final IToDoDAO toDoDAO;
    private final ICondivisioneDAO condivisioneDAO = new CondivisioneDAO();


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

        ToDo nuovoToDo = new ToDo("", "", "", null, "", "FFFFFF");

        ToDoFormDialog dialog = new ToDoFormDialog(nuovoToDo, true, this, getUtenteCorrente(), bacheca.getTipo());
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

        if (option == JOptionPane.YES_OPTION && bacheca != null) {
            boolean success = false;
            String prop = todo.getProprietario();

            if (prop != null && prop.equals(utenteCorrente.getUsername())) {
                boolean condivisioniEliminate = condivisioneDAO.eliminaCondivisioniCollegate(todo.getId());
                success = toDoDAO.elimina(todo.getId());
            } else if (prop != null) {
                // Sei un utente condiviso
                success = condivisioneDAO.rimuoviCondivisione(
                        utenteCorrente.getUsername(),
                        prop,
                        bacheca.getTipo().name(),
                        todo.getTitolo()
                );

                if (success) {
                    utenteCorrente.rimuoviToDoCondiviso(todo);
                }
            }
            if (success) {
                List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), bacheca.getTipo());
                bacheca.setToDoList(todos);

                board.clearToDos();
                board.refresh();
            } else {
                JOptionPane.showMessageDialog(board, "Errore durante l'eliminazione dal database.");
            }
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

    LocalDate oggi = LocalDate.now();

    for (TipoBacheca tipo : TipoBacheca.values()) {
        Bacheca b = bachecaDAO.findByTipo(username, tipo);
        if (b == null) continue;

        List<ToDo> todos = toDoDAO.trovaPerBacheca(username, tipo);
        StringBuilder avvisi = new StringBuilder();

        for (ToDo todo : todos) {
            if (todo.getDataDiScadenza() != null && !todo.getDataDiScadenza().isEmpty()) {
                try {
                    LocalDate dataScadenza = LocalDate.parse(todo.getDataDiScadenza());
                    if (!dataScadenza.isAfter(oggi)) {
                        if (todo.getStato() == StatoToDo.Completato) {
                            todo.setStato(StatoToDo.NonCompletato);
                            toDoDAO.aggiorna(todo, username, tipo);
                        }
                        if (todo.getStato() == StatoToDo.NonCompletato) {
                            avvisi.append("- ").append(todo.getTitolo()).append("\n");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Errore parsing data: " + todo.getDataDiScadenza());
                }
            }
        }

        b.setToDoList(todos);

        BoardPanel target = switch (tipo) {
            case Universita -> universitaBoard;
            case Lavoro -> lavoroBoard;
            case TempoLibero -> tempoLiberoBoard;
        };

        target.setBacheca(b);

        if (!avvisi.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Attenzione! I seguenti ToDo nella bacheca \"" + tipo.name() + "\" sono scaduti e non completati:\n\n" + avvisi,
                "ToDo Scaduti",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    List<ToDo> condivisi = toDoDAO.getToDoCondivisiCon(username);
    utenteCorrente.setToDoCondivisi(condivisi);
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
        ToDoFormDialog dialog = new ToDoFormDialog(todo, isEditable, this, getUtenteCorrente(), board.getBacheca().getTipo());
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(board);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Bacheca bacheca = board.getBacheca();
            toDoDAO.aggiorna(todo, utenteCorrente.getUsername(), bacheca.getTipo());

            // Ricarica tutti i ToDo della bacheca
            List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), bacheca.getTipo());
            bacheca.setToDoList(todos);
            board.setBacheca(bacheca); // triggera il refresh della GUI

            updateView.run(); // aggiorna la vista della board
        }
    }

    public boolean condividiToDo(String destinatario, ToDo todo, String prop, TipoBacheca tipo) {
        String usernameMittente = utenteCorrente.getUsername();
        String tipoString = tipo.name();
        String titolo = todo.getTitolo();

        if (destinatario.equals(usernameMittente)) {
            JOptionPane.showMessageDialog(null, "Non puoi condividere un ToDo con te stesso.");
            return false;
        }

        if (condivisioneDAO.esisteCondivisione(destinatario, prop, tipoString, titolo)) {
            JOptionPane.showMessageDialog(null, "Hai già condiviso questo ToDo con questo utente.");
            return false;
        }

        boolean success = condivisioneDAO.condividi(destinatario, prop, tipoString, titolo);
        if (success) {
            JOptionPane.showMessageDialog(null, "ToDo condiviso con successo.");
        } else {
            JOptionPane.showMessageDialog(null, "Errore durante la condivisione.");
        }

        return success;
    }


    public boolean isToDoCondiviso(String utente, ToDo todo, String prop, TipoBacheca tipo) {
        return condivisioneDAO.esisteCondivisione(utente, prop, tipo.name(), todo.getTitolo());
    }

    public boolean rimuoviCondivisione(String utente, ToDo todo, String prop, TipoBacheca tipo) {
        return condivisioneDAO.rimuoviCondivisione(utente, prop, tipo.name(), todo.getTitolo());
    }


    public boolean register(String username, String password) {
       username = username.trim();

       if (utenteDAO.findByUsername(username) != null) {
          return false;
      } else {
          Utente nuovoUtente = new Utente(username, password);
          boolean result = utenteDAO.salvaUtente(nuovoUtente);
          return result;
     }
    }

    // Carica l'utente e lo memorizza 
    public Utente getUtenteCorrente() {
       return utenteCorrente;
   }
   

   // Resetta l'utente corrente e svuota le bacheche, così il prossimo utente non vedrà i dati dell'utente precedente
   public void logout() {
    utenteCorrente = null;

    universitaBoard.setBacheca(null);
    universitaBoard.clearToDos();

    lavoroBoard.setBacheca(null);
    lavoroBoard.clearToDos();

    tempoLiberoBoard.setBacheca(null);
    tempoLiberoBoard.clearToDos();
  }

   // Metodo per il toggle dello stato di completamento di un ToDo
    public void toggleCompletamento(BoardPanel board, ToDo todo) {
        if (todo.getStato() == StatoToDo.Completato) {
            todo.setStato(StatoToDo.NonCompletato);
        } else {
            todo.setStato(StatoToDo.Completato);
        }

    

        // Aggiorna il ToDo nel database
        Bacheca bacheca = board.getBacheca();
        toDoDAO.aggiorna(todo, utenteCorrente.getUsername(), bacheca.getTipo());

        // Ricarica tutti i ToDo e aggiorna la GUI
        List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), bacheca.getTipo());
        bacheca.setToDoList(todos);
        board.setBacheca(bacheca);
    }


    // Metodo per ottenere le richieste pendenti per l'utente corrente
    public List<String[]> getRichiestePendenti() {
        if (utenteCorrente == null) return new ArrayList<>();
        // recupera da DAO (implementazione DAO deve ritornare le richieste)
        return ((CondivisioneDAO) condivisioneDAO).getRichiestePendentiPerUtente(utenteCorrente.getUsername());
    }

    // Qui la modifica importante per accettare la richiesta aggiornando lo stato
    public boolean accettaRichiesta(String destinatario, String proprietario, String tipoBacheca, String titoloToDo) {
        return ((CondivisioneDAO) condivisioneDAO).aggiornaStatoRichiesta(destinatario, proprietario, tipoBacheca, titoloToDo, "ACCEPTED");
    }

    // Qui rimuovi la richiesta semplicemente cancellando la riga o aggiornando stato a RIFIUTATO
    public boolean rifiutaRichiesta(String destinatario, String proprietario, String tipoBacheca, String titoloToDo) {
        return ((CondivisioneDAO) condivisioneDAO).aggiornaStatoRichiesta(destinatario, proprietario, tipoBacheca, titoloToDo, "REJECTED");
    }
    

    public Bacheca getBachecaPerTipo(TipoBacheca tipo) {
    // Cerca la bacheca in base al tipo, caricandola da DAO
    if (utenteCorrente == null) return null;

    Bacheca b = bachecaDAO.findByTipo(utenteCorrente.getUsername(), tipo);
    if (b != null) {
        List<ToDo> todos = toDoDAO.trovaPerBacheca(utenteCorrente.getUsername(), tipo);
        b.setToDoList(todos);
    }
    return b;
   }

    public List<ToDo> getToDoCondivisi(String username) {
        return ((ToDoDAO) toDoDAO).getToDoCondivisiCon(username);
    }

}