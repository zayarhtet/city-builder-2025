package view.component;
import view.MainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatisticPanel extends JPanel {

    private JLabel expensesLabel, satisfactionLabel, populationLabel, timeLabel;
    private JButton backToMenuButton;
    private final MainWindow frame;

    public StatisticPanel(MainWindow frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(1000, 50));
        setBackground(MainWindow.THEME);
        setLayout(null);

        expensesLabel = new JLabel("Expenses: $0");
        expensesLabel.setForeground(MainWindow.BG_COLOR);
        expensesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expensesLabel.setBounds(250, 10, 150, 30);
        add(expensesLabel);

        satisfactionLabel = new JLabel("Satisfaction: 0%");
        satisfactionLabel.setForeground(MainWindow.BG_COLOR);
        satisfactionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        satisfactionLabel.setBounds(425, 10, 150, 30);
        add(satisfactionLabel);

        populationLabel = new JLabel("Population: 0");
        populationLabel.setForeground(MainWindow.BG_COLOR);
        populationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        populationLabel.setBounds(625, 10, 150, 30);
        add(populationLabel);

        timeLabel = new JLabel("2023.04.11 22:10");
        timeLabel.setForeground(MainWindow.BG_COLOR);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(800, 10, 150, 30);
        add(timeLabel);

        backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setFont(new Font("Courier", Font.BOLD, 14));
        backToMenuButton.setBounds(50, 10, 150, 30);
        backToMenuButton.setBackground(MainWindow.BG_COLOR);
        backToMenuButton.setForeground(MainWindow.THEME);
        add(backToMenuButton);
        backToMenuButton.addActionListener(e -> {
            this.frame.showMenuPage();
            this.frame.hideMapPage();
            EventModel.DeleteInstance();
        });
    }
}