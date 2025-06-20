import gui.MainFrame;
import dao.InitDAO;

import javax.swing.*;

/**
 * Classe principale del progetto.
 * Avvia l'inizializzazione dei dati statici nel database e l'interfaccia grafica Swing.
 */
public class Main {

    /**
     * Metodo principale di avvio dell'applicazione.
     * <p>
     * Inizializza i tipi di bacheca e gli stati dei ToDo se non sono già presenti nel database,
     * poi avvia l'interfaccia grafica principale (MainFrame) usando il thread dell'Event Dispatch Thread di Swing.
     *
     * @param args eventuali argomenti da linea di comando (non usati)
     */
    public static void main(String[] args) {
        InitDAO.inserisciTipiEStati();

        // Avvia l'interfaccia grafica sul thread corretto di Swing
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

