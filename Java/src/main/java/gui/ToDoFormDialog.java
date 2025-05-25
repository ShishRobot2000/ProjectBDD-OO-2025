package gui;

import model.ToDo;

import javax.swing.*;
import java.awt.*;

public class ToDoFormDialog extends JDialog {

    private JTextField titoloField;
    private JTextField descrizioneField;
    private JTextField scadenzaField;
    private JTextField coloreField;

    private boolean confirmed = false;
    private boolean editable = true;

    private JButton btnSave;
    private JButton btnCancel;

    private ToDo todo;

    public ToDoFormDialog(ToDo todo, boolean editable) {
        this.todo = todo;
        this.editable = editable;

        setTitle("Dettagli ToDo: " + todo.getTitolo());
        setModal(true);

        // Dimensioni diverse a seconda della modalità
        if (editable) {
            setSize(400, 300);
        } else {
            setSize(500, 350);
        }
        setLocationRelativeTo(null);

        // Pannello principale
        JPanel content = new JPanel(new GridLayout(0, 2, 5, 5));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Imposta colore sfondo della card
        try {
            content.setBackground(Color.decode("#" + todo.getColore()));
        } catch (Exception e) {
            content.setBackground(Color.LIGHT_GRAY);
        }

        // Titolo
        content.add(new JLabel("Titolo:"));
        titoloField = new JTextField(todo.getTitolo());
        content.add(titoloField);

        // Descrizione
        content.add(new JLabel("Descrizione:"));
        descrizioneField = new JTextField(todo.getDescrizione());
        content.add(descrizioneField);

        // Scadenza
        content.add(new JLabel("Scadenza (YYYY-MM-DD):"));
        scadenzaField = new JTextField(todo.getDataDiScadenza());
        content.add(scadenzaField);

        // Solo se siamo in modalità modifica, mostriamo anche il campo "colore"
        if (editable) {
            content.add(new JLabel("Colore (es. FFFFFF):"));
            coloreField = new JTextField(todo.getColore());
            content.add(coloreField);
        }

        // Pannello pulsanti
        JPanel buttons = new JPanel();
        btnSave = new JButton("Salva");
        btnCancel = new JButton(editable ? "Annulla" : "Chiudi");

        btnSave.addActionListener(e -> {
            todo.setTitolo(titoloField.getText().trim());
            todo.setDescrizione(descrizioneField.getText().trim());
            todo.setDataDiScadenza(scadenzaField.getText().trim());
            if (editable) {
                todo.setColore(coloreField.getText().trim());
            }
            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        if (editable) buttons.add(btnSave);
        buttons.add(btnCancel);

        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        updateEditableState();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        updateEditableState();
    }

    private void updateEditableState() {
        titoloField.setEditable(editable);
        descrizioneField.setEditable(editable);
        scadenzaField.setEditable(editable);

        if (editable && coloreField != null) {
            coloreField.setEditable(true);
        }

        btnSave.setVisible(editable);
        btnCancel.setText(editable ? "Annulla" : "Chiudi");
    }
}

