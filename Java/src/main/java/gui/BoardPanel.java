package gui;

import model.ToDo;
import model.StatoToDo;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BoardPanel extends JPanel {

    private String boardName;

    public BoardPanel(String boardName) {
        this.boardName = boardName;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(boardName));
    }

    public String getBoardName() {
        return boardName;
    }
}

