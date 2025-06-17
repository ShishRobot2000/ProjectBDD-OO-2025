package gui;

import model.ToDo;
import controller.Controller;
import model.Utente;
import model.TipoBacheca;

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
    private Controller controller;
    private Utente utente;
    private TipoBacheca tipoBacheca;

    public ToDoFormDialog(ToDo todo, boolean editable, Controller controller, Utente utente, TipoBacheca tipoBacheca) {
        this.todo = todo;
        this.editable = editable;
        this.controller = controller;
        this.utente = utente;
        this.tipoBacheca = tipoBacheca;

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

        if (todo.getId() > 0 && (editable == false)) { // mostralo solo se è già stato salvato
            content.add(new JLabel("ID:"));
            JTextField idField = new JTextField(String.valueOf(todo.getId()));
            idField.setEditable(false);
            content.add(idField);
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

        // Se non è in modalità modifica, aggiungi il pulsante Condividi
        if (!editable) {
            JButton btnCondividi = new JButton("Condividi");
            btnCondividi.addActionListener(e -> {
                String destinatario = JOptionPane.showInputDialog(this, "Inserisci il nome utente con cui vuoi condividere:");
                if (destinatario != null && !destinatario.trim().isEmpty()) {
                    boolean esito = controller.condividiToDo(destinatario.trim(), todo, utente.getUsername(), tipoBacheca);
                    if (esito) {
                        JOptionPane.showMessageDialog(this, "ToDo condiviso con " + destinatario);
                    } else {
                        JOptionPane.showMessageDialog(this, "Errore nella condivisione. Verifica se il ToDo è già condiviso o se l'utente esiste.");
                    }
                }
            });
            buttons.add(btnCondividi);
        }

        buttons.add(btnSave);
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
