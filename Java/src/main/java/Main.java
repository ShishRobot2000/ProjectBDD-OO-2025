import gui.MainFrame;
import dao.InitDAO; // ora che è nel package dao

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Inizializza tipi bacheca e stati ToDo, solo se mancanti
        InitDAO.inserisciTipiEStati();
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}