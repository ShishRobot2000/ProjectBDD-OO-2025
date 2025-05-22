package gui;

import model.ToDo;

import javax.swing.*;
import java.awt.*;

public class ToDoCardPanel extends JPanel {

    public ToDoCardPanel(ToDo todo) {
        setLayout(new GridBagLayout()); // Centro verticale e orizzontale
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.decode("#" + todo.getColore()));

        setMaximumSize(new Dimension(200, 80));
        setPreferredSize(new Dimension(200, 80));

        JLabel titolo = new JLabel("<html><div style='text-align: center;'><b>" + todo.getTitolo() + "</b><br/>Scadenza: " + todo.getDataDiScadenza() + "</div></html>");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(titolo); // Non serve BorderLayout con GridBagLayout

        // Clic apri dialog
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ToDoFormDialog(todo).setVisible(true);
            }
        });
    }
}



