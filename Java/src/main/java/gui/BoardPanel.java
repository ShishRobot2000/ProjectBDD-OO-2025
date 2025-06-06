package gui;

import model.*;
import controller.*;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private String boardName;
    private Bacheca bacheca; // riferimento alla bacheca associata a questo pannello
    private JPanel toDoListPanel; // contiene i ToDoCardPanel

    private Controller controller; // controller condiviso
    private JButton addButton;     // pulsante per aggiungere ToDo

    // Costruttore che inizializza il pannello della bacheca
    public BoardPanel(String boardName, Bacheca bacheca, Controller controller) {
        this.boardName = boardName;
        this.bacheca = bacheca;
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(boardName));

        toDoListPanel = new JPanel();
        toDoListPanel.setLayout(new BoxLayout(toDoListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(toDoListPanel), BorderLayout.CENTER);

        // Inizializza il pulsante ma NON registrare ancora il listener
        addButton = new JButton("+ Aggiungi ToDo");
        add(addButton, BorderLayout.SOUTH);
    }

    // Imposta il controller dopo la costruzione e registra il listener
    public void setController(Controller controller) {
        this.controller = controller;

        // Registra il listener ora che il controller è disponibile
        addButton.addActionListener(e -> controller.addNewToDo(this));
    }

    // Metodo per impostare la bacheca e caricare i ToDo
    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;
        clearToDos();
        if (bacheca != null) {
            for (ToDo todo : bacheca.getToDoList()) {
                addToDo(todo);
            }
        }
    }

    // Crea un nuovo ToDoCardPanel e lo aggiunge al pannello
    public void addToDo(ToDo todo) {
        ToDoCardPanel card = new ToDoCardPanel(todo, this, controller);
        toDoListPanel.add(card);
        revalidate();
        repaint();
    }

    // Metodo per rimuovere un ToDo dalla BoardPanel
    public void removeToDo(ToDo todo) {
        controller.removeToDo(this, todo);
    }

    // Metodo per rimuovere tutti i ToDoCardPanel dal pannello
    public void clearToDos() {
        toDoListPanel.removeAll();
        revalidate();
        repaint();
    }

    // Metodo per ottenere la bacheca associata a questo pannello
    public Bacheca getBacheca() {
        return bacheca;
    }

    // Metodo per ottenere il nome della bacheca
    public String getBoardName() {
        return boardName;
    }

}

