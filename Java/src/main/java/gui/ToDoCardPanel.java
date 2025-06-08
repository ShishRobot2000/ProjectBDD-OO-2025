package gui;

import model.ToDo;
import model.StatoToDo;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoCardPanel extends JPanel {

    private ToDo todo;
    private BoardPanel parent;
    private Controller controller;

    public ToDoCardPanel(ToDo todo, BoardPanel boardPanel, Controller controller) {
        this.todo = todo;
        this.parent = boardPanel;
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        try {
            setBackground(Color.decode("#" + todo.getColore()));
        } catch (Exception e) {
            setBackground(Color.WHITE);
        }

        setMaximumSize(new Dimension(250, 100));
        setPreferredSize(new Dimension(250, 100));

        JLabel titolo = new JLabel("<html><div style='text-align: center;'><b>" + todo.getTitolo() +
                "</b><br/>Scadenza: " + todo.getDataDiScadenza() + "</div></html>");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(titolo, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        // Checkbox per lo stato completato
        JCheckBox checkCompletato = new JCheckBox("Completato");
        checkCompletato.setSelected(todo.getStato() == StatoToDo.Completato);
        checkCompletato.addActionListener(e -> {
            controller.toggleCompletamento(parent, todo);
        });

        JButton btnEdit = new JButton("Modifica");
        JButton btnRemove = new JButton("Rimuovi");

        btnEdit.addActionListener(e -> openEditDialog(true));
        btnRemove.addActionListener(e -> removeToDo());

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(checkCompletato);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnRemove);

        add(buttonPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openEditDialog(false);
                }
            }
        });
    }

    private void openEditDialog(boolean isEditable) {
        controller.editToDo(parent, todo, isEditable, this::updateCardContent);
    }

    private void updateCardContent() {
        try {
            setBackground(Color.decode("#" + todo.getColore()));
        } catch (Exception e) {
            setBackground(Color.WHITE);
        }

        JLabel titolo = new JLabel("<html><div style='text-align: center;'><b>" + todo.getTitolo() +
                "</b><br/>Scadenza: " + todo.getDataDiScadenza() + "</div></html>");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        removeAll();
        setLayout(new BorderLayout());
        add(titolo, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JCheckBox checkCompletato = new JCheckBox("Completato");
        checkCompletato.setSelected(todo.getStato() == StatoToDo.Completato);
        checkCompletato.addActionListener(e -> {
            controller.toggleCompletamento(parent, todo);
        });

        JButton btnEdit = new JButton("Modifica");
        JButton btnRemove = new JButton("Rimuovi");

        btnEdit.addActionListener(e -> openEditDialog(true));
        btnRemove.addActionListener(e -> removeToDo());

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(checkCompletato);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnRemove);

        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void removeToDo() {
        if (parent != null) {
            parent.removeToDo(todo);
        }
    }
}
