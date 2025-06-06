package gui;
import javax.swing.*;
import java.awt.*;
public class SearchPanel extends JPanel {
    public SearchPanel() {
        setLayout(new FlowLayout());
        add(new JLabel("Cerca:"));
        add(new JTextField(20));
    }
}