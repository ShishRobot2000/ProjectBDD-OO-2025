package gui;

import model.ToDo;
import controller.Controller;
import model.Utente;
import model.TipoBacheca;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

/**
 * Finestra di dialogo per la visualizzazione o modifica di un ToDo.
 * Supporta la visualizzazione di dettagli, la modifica del contenuto e la condivisione con altri utenti.
 */
public class ToDoFormDialog extends JDialog {

    private JTextField titoloField;
    private JTextField descrizioneField;
    private JTextField scadenzaField;
    private JTextField coloreField;
    private JTextField urlField;
    private JLabel imagePreviewLabel;
    private byte[] imageBytes;

    private boolean confirmed = false;
    private boolean editable = true;

    private JButton btnSave;
    private JButton btnCancel;

    private ToDo todo;
    private Controller controller;
    private Utente utente;
    private TipoBacheca tipoBacheca;

    /**
     * Costruttore della finestra di dialogo per visualizzare o modificare un ToDo.
     *
     * @param todo il ToDo da gestire
     * @param editable true per abilitare la modifica, false per sola lettura
     * @param controller il controller del progetto
     * @param utente l'utente attualmente autenticato
     * @param tipoBacheca il tipo di bacheca a cui il ToDo appartiene
     */
    public ToDoFormDialog(ToDo todo, boolean editable, Controller controller, Utente utente, TipoBacheca tipoBacheca) {
        this.todo = todo;
        this.editable = editable;
        this.controller = controller;
        this.utente = utente;
        this.tipoBacheca = tipoBacheca;

        setTitle("Dettagli ToDo: " + todo.getTitolo());
        setModal(true);
        setSize(editable ? 500 : 600, 500);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridLayout(0, 2, 5, 5));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        try {
            content.setBackground(Color.decode("#" + todo.getColore()));
        } catch (Exception e) {
            content.setBackground(Color.LIGHT_GRAY);
        }

        if (todo.getId() > 0 && !editable) {
            content.add(new JLabel("ID:"));
            JTextField idField = new JTextField(String.valueOf(todo.getId()));
            idField.setEditable(false);
            content.add(idField);
        }

        content.add(new JLabel("Titolo:"));
        titoloField = new JTextField(todo.getTitolo());
        content.add(titoloField);

        content.add(new JLabel("Descrizione:"));
        descrizioneField = new JTextField(todo.getDescrizione());
        content.add(descrizioneField);

        content.add(new JLabel("Scadenza (YYYY-MM-DD):"));
        scadenzaField = new JTextField(todo.getDataDiScadenza());
        content.add(scadenzaField);

        content.add(new JLabel("URL:"));
        if (!editable) {
            if (todo.getUrl() != null && !todo.getUrl().isBlank()) {
                JLabel urlLabel = new JLabel("<html><a href='" + todo.getUrl() + "'>" + todo.getUrl() + "</a></html>");
                urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                urlLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        try {
                            String rawUrl = todo.getUrl().trim();
                            if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
                                rawUrl = "https://" + rawUrl;
                            }
                            Desktop.getDesktop().browse(new java.net.URI(rawUrl));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Errore nell'apertura dell'URL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                content.add(urlLabel);
            } else {
                content.add(new JLabel("Nessun URL disponibile"));
            }
        } else {
            urlField = new JTextField(todo.getUrl() != null ? todo.getUrl() : "");
            content.add(urlField);
        }

        content.add(new JLabel("Immagine:"));
        if (!editable) {
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePreviewLabel = new JLabel();
            imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            if (todo.getImmagine() != null && todo.getImmagine().length > 0) {
                try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(todo.getImmagine()));
                    Image scaled = img.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaled));
                } catch (IOException e) {
                    imagePreviewLabel.setText("Errore caricamento immagine");
                }
            } else {
                imagePreviewLabel.setText("Nessuna immagine disponibile");
            }

            imagePanel.add(imagePreviewLabel, BorderLayout.CENTER);

            if (todo.getImmagine() != null && todo.getImmagine().length > 0) {
                JButton openImageBtn = new JButton("Apri a dimensione piena");
                openImageBtn.addActionListener(e -> {
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(todo.getImmagine()));
                        JLabel fullImageLabel = new JLabel(new ImageIcon(img));
                        JScrollPane scrollPane = new JScrollPane(fullImageLabel);
                        scrollPane.setPreferredSize(new Dimension(
                                Math.min(img.getWidth(), 1200),
                                Math.min(img.getHeight(), 800)));
                        JDialog dialog = new JDialog(ToDoFormDialog.this, "Immagine completa", false);
                        dialog.setAlwaysOnTop(true);
                        dialog.getContentPane().add(scrollPane);
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Errore nel caricamento dell'immagine.");
                    }
                });
                imagePanel.add(openImageBtn, BorderLayout.SOUTH);
            }

            content.add(imagePanel);
        } else {
            JButton imageButton = new JButton("Seleziona Immagine");
            imageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        imageBytes = Files.readAllBytes(file.toPath());
                        JOptionPane.showMessageDialog(this, "Immagine caricata con successo.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Errore nel caricamento dell'immagine.");
                    }
                }
            });
            content.add(imageButton);
        }
        
        if (editable) {
            content.add(new JLabel("Colore (es. FFFFFF):"));
            coloreField = new JTextField(todo.getColore());
            content.add(coloreField);
        }

        JPanel buttons = new JPanel();
        btnSave = new JButton("Salva");
        btnCancel = new JButton(editable ? "Annulla" : "Chiudi");

        btnSave.addActionListener(e -> {
            todo.setTitolo(titoloField.getText().trim());
            todo.setDescrizione(descrizioneField.getText().trim());
            todo.setDataDiScadenza(scadenzaField.getText().trim());

            if (editable) {
                if (coloreField != null) {
                    todo.setColore(coloreField.getText().trim());
                }
                if (urlField != null) {
                    todo.setUrl(urlField.getText().trim());
                }
                if (imageBytes != null) {
                    todo.setImmagine(imageBytes);
                }
            }

            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        if (!editable && todo.getProprietario() != null && todo.getProprietario().equals(utente.getUsername())) {
            JButton btnCondividi = new JButton("Condividi");
            btnCondividi.addActionListener(e -> {
                String destinatario = JOptionPane.showInputDialog(this, "Inserisci il nome utente con cui vuoi condividere:");
                if (destinatario != null && !destinatario.trim().isEmpty()) {
                    boolean esito = controller.condividiToDo(
                            destinatario.trim(),
                            todo,
                            utente.getUsername(),
                            tipoBacheca
                    );
                    if (esito) {
                        JOptionPane.showMessageDialog(this, "ToDo condiviso con " + destinatario);
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

    /**
     * Indica se l'utente ha confermato il salvataggio del ToDo.
     *
     * @return true se confermato, false altrimenti
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Imposta la modalità di modifica del form.
     *
     * @param editable true per abilitare la modifica, false per sola lettura
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        updateEditableState();
    }

    /**
     * Aggiorna l'interfaccia in base alla modalità (modifica o lettura).
     */
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
