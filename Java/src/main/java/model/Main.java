package model;

import gui.MainFrame;  // Assicurati che il package sia corretto

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();  // Avvia la finestra principale
        });
    }
}

