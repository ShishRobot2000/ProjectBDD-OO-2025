package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import model.Utente;
import model.Bacheca;
import model.ToDo;
import model.TipoBacheca;
import service.AuthService;

public class DashboardPanel extends JPanel {

    private MainFrame parent;

    private JPanel universitaPanel;
    private JPanel lavoroPanel;
    private JPanel tempoLiberoPanel;

    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        JPanel boards = new JPanel(new GridLayout(1, 3, 10, 10));

        universitaPanel = createBoardPanel("Università");
        lavoroPanel = createBoardPanel("Lavoro");
        tempoLiberoPanel = createBoardPanel("Tempo Libero");

        boards.add(universitaPanel);
        boards.add(lavoroPanel);
        boards.add(tempoLiberoPanel);

        add(boards, BorderLayout.CENTER);
    }

    private JPanel createBoardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        border.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(border);

        return panel;
    }

    public void loadUser(String username) {
        Utente utente = AuthService.getUtente(username);

        // Pulisce i pannelli
        universitaPanel.removeAll();
        lavoroPanel.removeAll();
        tempoLiberoPanel.removeAll();

        // Aggiunge i ToDo dalle bacheche dell’utente
        for (Bacheca b : utente.getBacheche()) {
            JPanel target = switch (b.getTipo()) {
                case Università -> universitaPanel;
                case Lavoro -> lavoroPanel;
                case TempoLibero -> tempoLiberoPanel;
            };

            for (ToDo t : b.getToDoList()) {
                target.add(new ToDoCardPanel(t));
            }
        }

        revalidate();
        repaint();
    }
}


