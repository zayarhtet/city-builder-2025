package view.component;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticPanel extends JPanel {
    private JButton backToMenu;

    private MainWindow frame;

    public StatisticPanel(MainWindow frame, Color theme) {
        setPreferredSize(new Dimension(1000,50));
        setBackground(theme);

        this.frame = frame;

        setLayout(new GridLayout(1,5));
        add(backToMenu = new JButton("Back To Menu"));
        add(new JLabel("Expenses"));
        add(new JLabel("Satisfaction"));
        add(new JLabel("Population"));
        add(new JLabel("TIME"));

        backToMenu.setPreferredSize(new Dimension(50,10));
        backToMenu.addActionListener(e -> {
            //TODO Should Save everything first!
            this.frame.showMenuPage();
            //TODO Do we have to delete the Object?
            this.frame.hideMapPage();
        });
        backToMenu.setOpaque(false);
        backToMenu.setContentAreaFilled(false);

        //  add JLabel
    }
}
