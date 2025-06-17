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

public class DashboardPanel extends JPanel {

    private MainFrame parent;

    // Pannelli per le bacheche
    private BoardPanel universitaBoard;
    private BoardPanel lavoroBoard;
    private BoardPanel tempoLiberoBoard;

    private Controller controller; // controller MVC

    private JLabel welcomeLabel; // Label per il messaggio benvenuto 

    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        
        // Label benvenuto con padding
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("Benvenuto:");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            parent.showLoginPanel(); // Torna alla schermata di login 
        });

        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton invitiButton = new JButton("Inviti");
        invitiButton.addActionListener(e -> {
            JDialog dialog = new InvitationsDialog(parent, controller.getUtenteCorrente(), this);
            dialog.setVisible(true);
        });
        rightButtonsPanel.add(invitiButton);
        rightButtonsPanel.add(logoutButton);

        topPanel.add(rightButtonsPanel, BorderLayout.EAST);


        add(topPanel, BorderLayout.NORTH);

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
    
    public void setWelcomeUser(String username) {
        welcomeLabel.setText("Benvenuto: " + username);
    }


    // Metodo per caricare l'utente e le sue bacheche
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
