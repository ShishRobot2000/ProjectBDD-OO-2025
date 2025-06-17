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
    private Utente utenteCorrente; // riferimento all'utente corrente

    // Costruttore che inizializza il pannello della bacheca
    public BoardPanel(String boardName, Bacheca bacheca, Controller controller, Utente utenteCorrente) {
        this.boardName = boardName;
        this.bacheca = bacheca;
        this.controller = controller;
        this.utenteCorrente = utenteCorrente;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(boardName));

        toDoListPanel = new JPanel();
        toDoListPanel.setLayout(new BoxLayout(toDoListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(toDoListPanel), BorderLayout.CENTER);

        addButton = new JButton("+ Aggiungi ToDo");
        add(addButton, BorderLayout.SOUTH);

        // Registra subito il listener sul pulsante
        //addButton.addActionListener(e -> controller.addNewToDo(this));
        
        if (controller != null) {
             addButton.addActionListener(e -> controller.addNewToDo(this));
       }
    }
    

    // Setter per l’utente corrente (se vuoi modificarlo dopo la costruzione)
    public void setUtenteCorrente(Utente utente) {
        this.utenteCorrente = utente;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;

        // Rimuove eventuali listener precedenti per evitare duplicati
        for (var al : addButton.getActionListeners()) {
             addButton.removeActionListener(al);
       }

       if (controller != null) {
             addButton.addActionListener(e -> controller.addNewToDo(this));
        }
    }

    // Metodo per impostare la bacheca e caricare i ToDo
    public void setBacheca(Bacheca bacheca) {
        this.bacheca = bacheca;

    }
    

    public void aggiornaBoard() {
        refresh();
    }

    
    // Metodo per aggiungere una ToDoCardPanel al pannello
    public void addToDo(ToDo todo) {
        ToDoCardPanel card = new ToDoCardPanel(todo, this, controller, utenteCorrente);
        toDoListPanel.add(card);
        revalidate();
        repaint();
    }

    // Metodo per rimuovere un ToDo dalla BoardPanel tramite il controller
    public void removeToDo(ToDo todo) {
        controller.removeToDo(this, todo);
    }

    // Metodo per rimuovere tutti i ToDoCardPanel dal pannello
    public void clearToDos() {
        toDoListPanel.removeAll();
        revalidate();
        repaint();
    }

    public void refresh() {
        clearToDos();

        if (bacheca != null) {
            for (ToDo todo : bacheca.getToDoList()) {
                addToDo(todo);
            }
        }

        if (utenteCorrente != null && bacheca != null) {
            for (ToDo todo : utenteCorrente.getToDoCondivisi()) {
                // Aggiungi solo se appartiene alla bacheca corrente
                if (todo.getTipoBacheca() == bacheca.getTipo()) {
                    // Evita duplicati
                    if (!bacheca.getToDoList().contains(todo)) {
                        addToDo(todo);
                    }
                }
            }
        }
    }

    // Getters
    public Bacheca getBacheca() {
        return bacheca;
    }

    public String getBoardName() {
        return boardName;
    }
}