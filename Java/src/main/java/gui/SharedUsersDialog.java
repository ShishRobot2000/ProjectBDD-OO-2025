package gui;
import javax.swing.*;
import java.awt.*;
class SharedUsersDialog extends JDialog {
    public SharedUsersDialog(JFrame parent) {
        super(parent, "Utenti Condivisi", true);
        setSize(300, 200);
        setLayout(new BorderLayout());
        add(new JLabel("Lista utenti condivisi"), BorderLayout.CENTER);
        setLocationRelativeTo(parent);
    }
}