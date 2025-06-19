package gui;

import model.ToDo;
import model.Utente;
import model.StatoToDo;
import controller.Controller;
import dao.CondivisioneDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoCardPanel extends JPanel {

    private ToDo todo;
    private BoardPanel parent;
    private Controller controller;
    private Utente utenteCorrente;

    public ToDoCardPanel(ToDo todo, BoardPanel boardPanel, Controller controller, Utente utenteCorrente) {
        this.todo = todo;
        this.parent = boardPanel;
        this.controller = controller;
        this.utenteCorrente = utenteCorrente;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        try {
            setBackground(Color.decode("#" + todo.getColore()));
        } catch (Exception e) {
            setBackground(Color.WHITE);
        }

        setMaximumSize(new Dimension(300, 100));
        setPreferredSize(new Dimension(300, 100));

        boolean isCondiviso = utenteCorrente.getToDoCondivisi().contains(todo);

        JLabel titolo = new JLabel("<html><div style='text-align: center;'><b>" + todo.getTitolo() +
                "</b><br/>Scadenza: " + todo.getDataDiScadenza() + "</div></html>");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(titolo, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JCheckBox checkCompletato = new JCheckBox("Completato");
        checkCompletato.setSelected(todo.getStato() == StatoToDo.Completato);
        checkCompletato.setEnabled(!isCondiviso);
        if (isCondiviso) {
            checkCompletato.setToolTipText("Non puoi modificare un ToDo condiviso");
        }
        checkCompletato.addActionListener(e -> {
            if (!isCondiviso) {
                controller.toggleCompletamento(parent, todo);
            }
        });

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(checkCompletato);

        if (!isCondiviso) {
            JButton btnEdit = new JButton("Modifica");
            btnEdit.addActionListener(e -> openEditDialog(true));
            buttonPanel.add(btnEdit);
        }

        JButton btnRemove = new JButton(isCondiviso ? "Rimuovi Condivisione" : "Rimuovi");
        btnRemove.addActionListener(e -> {
            if (isCondiviso) {
                int conferma = JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro di voler rimuovere questa condivisione?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION
                );

                if (conferma == JOptionPane.YES_OPTION) {
                    CondivisioneDAO dao = new CondivisioneDAO();
                    boolean success = dao.rimuoviCondivisione(
                            utenteCorrente.getUsername(),
                            todo.getProprietario(),
                            todo.getTipoBacheca().name(),
                            todo.getTitolo()
                    );

                    if (success) {
                        utenteCorrente.rimuoviToDoCondiviso(todo);
                        if (parent != null) {
                            parent.refresh();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Errore durante la rimozione della condivisione dal database", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                removeToDo();
            }

        });
        buttonPanel.add(btnRemove);

        add(buttonPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ToDoFormDialog dialog = new ToDoFormDialog(
                            todo,
                            false, // modalità sola lettura
                            controller,
                            utenteCorrente,
                            todo.getTipoBacheca()
                    );
                    dialog.setVisible(true);
                }
            }
        });

    }

    private void openEditDialog(boolean isEditable) {
        controller.editToDo(parent, todo, isEditable, this::updateCardContent);
    }

    private void updateCardContent() {
        boolean isCondiviso = utenteCorrente.getToDoCondivisi().contains(todo);

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
        checkCompletato.setEnabled(!isCondiviso);
        if (isCondiviso) {
            checkCompletato.setToolTipText("Non puoi modificare un ToDo condiviso");
        }
        checkCompletato.addActionListener(e -> {
            if (!isCondiviso) {
                controller.toggleCompletamento(parent, todo);
            }
        });

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(checkCompletato);

        if (!isCondiviso) {
            JButton btnEdit = new JButton("Modifica");
            btnEdit.addActionListener(e -> openEditDialog(true));
            buttonPanel.add(btnEdit);
        }

        JButton btnRemove = new JButton(isCondiviso ? "Rimuovi Condivisione" : "Rimuovi");
        btnRemove.addActionListener(e -> {
            if (isCondiviso) {
                utenteCorrente.rimuoviToDoCondiviso(todo);
                if (parent != null) {
                    parent.refresh();
                }
            } else {
                removeToDo();
            }
        });
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
