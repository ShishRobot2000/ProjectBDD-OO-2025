package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import dao.CondivisioneDAO;
import model.Utente;

public class InvitationsDialog extends JDialog {

    private Utente currentUser;
    private CondivisioneDAO condivisioneDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private DashboardPanel dashboard;


    public InvitationsDialog(JFrame parent, Utente currentUser, DashboardPanel dashboard) {
        super(parent, "Richieste di Partecipazione", true);
        this.currentUser = currentUser;
        this.condivisioneDAO = new CondivisioneDAO();
        this.dashboard = dashboard;

        setSize(500, 300);
        setLocationRelativeTo(parent);

        initUI();

        loadRequests();
    }

    private void initUI() {
        // Colonne: Richiedente, Tipo Bacheca, Titolo ToDo
        String[] columns = {"Richiedente", "Tipo Bacheca", "Titolo ToDo"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Rendi non editabile la tabella
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton acceptButton = new JButton("Accetta");
        JButton rejectButton = new JButton("Rifiuta");

        acceptButton.addActionListener(e -> handleRequest("ACCEPTED"));
        rejectButton.addActionListener(e -> handleRequest("REJECTED"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRequests() {
        tableModel.setRowCount(0); // pulisci la tabella
        List<String[]> richieste = condivisioneDAO.getRichiestePendentiPerUtente(currentUser.getUsername());

        for (String[] r : richieste) {
            tableModel.addRow(r);
        }
    }

    private void handleRequest(String newStatus) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona una richiesta dalla tabella.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String proprietario = (String) tableModel.getValueAt(selectedRow, 0);
        String tipoBacheca = (String) tableModel.getValueAt(selectedRow, 1);
        String titolo = (String) tableModel.getValueAt(selectedRow, 2);

        boolean success = condivisioneDAO.aggiornaStatoRichiesta(currentUser.getUsername(), proprietario, tipoBacheca, titolo, newStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Richiesta " + (newStatus.equals("ACCEPTED") ? "accettata" : "rifiutata") + " con successo.");
            if (dashboard != null) {
                dashboard.loadUser(currentUser.getUsername());
            }
            loadRequests();
        } else {
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento della richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
