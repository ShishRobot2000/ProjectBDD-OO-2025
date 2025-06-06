package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import controller.*;

public class LoginPanel extends JPanel {

    private Controller controller = new Controller(null, null, null); // Non usiamo i board qui

    public LoginPanel(MainFrame parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(89, 25));

        // Listener delegato al controller
        loginButton.addActionListener((ActionEvent e) -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            controller.login(user, pass, parent);
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

        JButton registerButton = new JButton("Registrati");
        registerButton.addActionListener(e -> {
               new RegistrationDialog(parent, controller); // Apri la finestra di registrazione
     });

        gbc.gridy = 3;
        add(registerButton, gbc);
    }
}

