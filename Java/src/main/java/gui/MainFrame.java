package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame() {
        setTitle("ToDo Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centra la finestra

        // Inizializza i pannelli
        loginPanel = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);

        // Aggiungi le viste al CardLayout
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Mostra login all'avvio

        setVisible(true);
    }

    /**
     * Passa alla dashboard e carica i dati per l'utente autenticato
     */
    public void showDashboard(String username) {
        SwingUtilities.invokeLater(() -> dashboardPanel.loadUser(username));
        cardLayout.show(mainPanel, "Dashboard");
    }

    /**
     * Torna alla schermata di login (es. dopo logout)
     */
    public void showLoginPanel() {
        loginPanel.clearFields();
        cardLayout.show(mainPanel, "Login");
    }

}
