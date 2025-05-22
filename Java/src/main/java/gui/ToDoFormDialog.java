package gui;

import model.ToDo;

import javax.swing.*;
import java.awt.*;

public class ToDoFormDialog extends JDialog {

    public ToDoFormDialog(ToDo todo) {
        setTitle("Dettagli ToDo: " + todo.getTitolo());
        setModal(true);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Usa un pannello personalizzato per colorare lo sfondo
        JPanel content = new JPanel(new GridLayout(0, 1));
        content.setBackground(Color.decode("#" + todo.getColore()));
        content.setOpaque(true);

        content.add(new JLabel("Titolo: " + todo.getTitolo()));
        content.add(new JLabel("Descrizione: " + todo.getDescrizione()));
        content.add(new JLabel("Scadenza: " + todo.getDataDiScadenza()));
        content.add(new JLabel("Stato: " + todo.getStato().name()));

        add(content);
    }
}

