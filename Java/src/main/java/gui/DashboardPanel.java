package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import gui.BoardPanel;
import model.Utente;
import model.Bacheca;
import model.TipoBacheca;
import model.ToDo;
import controller.*;

/**
 * Pannello principale della dashboard utente, contenente le tre bacheche
 * (Università, Lavoro, Tempo Libero) e i pulsanti di controllo (logout, inviti).
 */
public class DashboardPanel extends JPanel {

    /** Riferimento alla finestra principale (MainFrame) */
    private MainFrame parent;

    /** Pannelli per le tre bacheche */
    private BoardPanel universitaBoard;
    private BoardPanel lavoroBoard;
    private BoardPanel tempoLiberoBoard;

    /** Controller MVC per gestire la logica dell'app */
    private Controller controller;

    /** Label per il messaggio di benvenuto */
    private JLabel welcomeLabel;

    /**
     * Costruttore del pannello dashboard.
     *
     * @param parent riferimento al MainFrame principale
     */
    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        // Pannello superiore con messaggio di benvenuto e pulsanti
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("Benvenuto:");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            parent.showLoginPanel(); // Torna alla schermata di login
        });

        JButton invitiButton = new JButton("Inviti");
        invitiButton.addActionListener(e -> {
            JDialog dialog = new InvitationsDialog(parent, controller.getUtenteCorrente(), this);
            dialog.setVisible(true);
        });

        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonsPanel.add(invitiButton);
        rightButtonsPanel.add(logoutButton);
        topPanel.add(rightButtonsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Pannello centrale con le tre bacheche
        JPanel boards = new JPanel(new GridLayout(1, 3, 10, 10));

        universitaBoard = new BoardPanel("Università", null, null, null);
        lavoroBoard = new BoardPanel("Lavoro", null, null, null);
        tempoLiberoBoard = new BoardPanel("Tempo Libero", null, null, null);

        controller = new Controller(universitaBoard, lavoroBoard, tempoLiberoBoard);

        universitaBoard.setController(controller);
        lavoroBoard.setController(controller);
        tempoLiberoBoard.setController(controller);

        boards.add(universitaBoard);
        boards.add(lavoroBoard);
        boards.add(tempoLiberoBoard);

        add(boards, BorderLayout.CENTER);
    }

    /**
     * Aggiorna il messaggio di benvenuto nella parte superiore della dashboard.
     *
     * @param username nome dell'utente corrente
     */
    public void setWelcomeUser(String username) {
        welcomeLabel.setText("Benvenuto: " + username);
    }

    /**
     * Carica l'utente corrente e aggiorna le tre bacheche con i relativi ToDo.
     *
     * @param username username dell’utente da caricare
     */
    public void loadUser(String username) {
        controller.loadUser(username);

        Utente utente = controller.getUtenteCorrente();

        if (utente != null) {
            setWelcomeUser(utente.getUsername());

            universitaBoard.setUtenteCorrente(utente);
            universitaBoard.setBacheca(controller.getBachecaPerTipo(TipoBacheca.Universita));
            universitaBoard.aggiornaBoard();

            lavoroBoard.setUtenteCorrente(utente);
            lavoroBoard.setBacheca(controller.getBachecaPerTipo(TipoBacheca.Lavoro));
            lavoroBoard.aggiornaBoard();

            tempoLiberoBoard.setUtenteCorrente(utente);
            tempoLiberoBoard.setBacheca(controller.getBachecaPerTipo(TipoBacheca.TempoLibero));
            tempoLiberoBoard.aggiornaBoard();
        } else {
            setWelcomeUser("ospite");
        }
    }

}

