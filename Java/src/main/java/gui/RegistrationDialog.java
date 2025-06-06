package gui;

import javax.swing.*;
import java.awt.*;
import controller.Controller;

public class RegistrationDialog extends JDialog {

    public RegistrationDialog(JFrame parent, Controller controller) {
        super(parent, "Registrazione", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("Nuovo Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Nuova Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton registerButton = new JButton("Registrati");
        registerButton.setPreferredSize(new Dimension(120, 25));

        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            boolean success = controller.register(user, pass);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore: Username già esistente.");
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
        add(registerButton, gbc);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
