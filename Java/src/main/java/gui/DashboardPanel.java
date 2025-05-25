package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import model.Utente;
import model.Bacheca;
import model.ToDo;
import model.TipoBacheca;
import service.AuthService;

import controller.*;

// Classe che estende JPanel, rappresenta un pannello grafico personalizzato
public class DashboardPanel extends JPanel {

    private MainFrame parent;

    // Pannelli per le bacheche
    private BoardPanel universitaBoard;
    private BoardPanel lavoroBoard;
    private BoardPanel tempoLiberoBoard;

    private Controller controller; // controller MVC

    // Pannello principale e imposta il layout
    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        JPanel boards = new JPanel(new GridLayout(1, 3, 10, 10));

        universitaBoard = new BoardPanel("Università", null, null);
        lavoroBoard = new BoardPanel("Lavoro", null, null);
        tempoLiberoBoard = new BoardPanel("Tempo Libero", null, null);

        // Inizializza il controller con i 3 BoardPanel
        controller = new Controller(universitaBoard, lavoroBoard, tempoLiberoBoard);

        // 3. ASSEGNA il controller ai board (usa metodo setController che ti consiglio di aggiungere)
        universitaBoard.setController(controller);
        lavoroBoard.setController(controller);
        tempoLiberoBoard.setController(controller);

        boards.add(universitaBoard);
        boards.add(lavoroBoard);
        boards.add(tempoLiberoBoard);

        add(boards, BorderLayout.CENTER);
    }

    // Metodo pubblico per caricare i dati di un utente
    public void loadUser(String username) {
        controller.loadUser(username);
    }
}



