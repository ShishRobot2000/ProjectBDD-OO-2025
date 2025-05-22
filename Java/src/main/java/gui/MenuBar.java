package gui;
import javax.swing.*;
import java.awt.*;
class MenuBar extends JMenuBar {
    public MenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        fileMenu.add(logoutItem);
        add(fileMenu);
    }
}
