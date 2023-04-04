package view.component;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticPanel extends JPanel {
    JButton backToMenu;
    MainWindow frame;

    public StatisticPanel(MainWindow frame, Color theme) {
        setPreferredSize(new Dimension(1000,50));
        setBackground(theme);

        this.frame = frame;

        backToMenu = new JButton("Back To Menu");
        this.add(backToMenu);

        backToMenu.addActionListener(e -> {
            //TODO Should Save everything first!
            this.frame.showMenuPage();
            //TODO Do we have to delete the Object?
            this.frame.hideMapPage();
        });
        //  add JLabel
    }
}
