package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    public LoginPanel(MainFrame parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener((ActionEvent e) -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("1234")) { // Sostituisci con chiamata a servizio auth
                parent.showDashboard(user);
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate");
            }
        });

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        add(passLabel, gbc);
        gbc.gridx = 1;
        add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginButton, gbc);
    }
}
