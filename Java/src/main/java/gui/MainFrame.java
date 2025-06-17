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
        setLocationRelativeTo(null);

        loginPanel = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");

        setVisible(true);
    }

    public void showDashboard(String username) {
        SwingUtilities.invokeLater(() -> dashboardPanel.loadUser(username)); // Poi carica i dati con eventuali popup
        cardLayout.show(mainPanel, "Dashboard");
    }

    public void showLoginPanel() {
        loginPanel.clearFields();
        cardLayout.show(mainPanel, "Login");
    }

}