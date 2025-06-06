package gui;

import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private DashboardPanel dashboardPanel;

    public MainFrame()
    {
        setTitle("ToDo Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        LoginPanel loginPanel = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");

        setVisible(true);
    }
    

    // Serve per caricare i dati dell'utente e mostrare il dashboard
    public void showDashboard(String username) {
        dashboardPanel.loadUser(username);
        cardLayout.show(mainPanel, "Dashboard");
    }


    // Mostra il pannello di login
    public void showLoginPanel() {
       cardLayout.show(mainPanel, "Login"); 
  }
}