package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra di dialogo modale che mostra la lista degli utenti con cui è stato condiviso un ToDo.
 * Attualmente contiene solo un messaggio statico, ma può essere estesa per visualizzare una lista reale.
 */
public class SharedUsersDialog extends JDialog {

    /**
     * Costruttore del dialogo.
     *
     * @param parent il frame genitore della finestra di dialogo
     */
    public SharedUsersDialog(JFrame parent) {
        super(parent, "Utenti Condivisi", true);
        setSize(300, 200);
        setLayout(new BorderLayout());

        // Messaggio statico (può essere sostituito con una lista dinamica di utenti)
        add(new JLabel("Lista utenti condivisi", SwingConstants.CENTER), BorderLayout.CENTER);

        setLocationRelativeTo(parent);
    }
}
