package com.coffee.citybuilder.view.component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.view.MainWindow;

public class StatisticPanel extends JPanel {

    private JLabel budgetLabel, satisfactionLabel, populationLabel, timeLabel;
    private JButton backToMenuButton;
    private final MainWindow frame;
    private City city;

    public StatisticPanel(MainWindow frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(1000, 50));
        setBackground(MainWindow.THEME);
        setLayout(null);

        budgetLabel = new JLabel("Expenses: $0");
        budgetLabel.setForeground(MainWindow.BG_COLOR);
        budgetLabel.setFont(new Font("Arial", Font.BOLD, 16));
        budgetLabel.setBounds(250, 10, 150, 30);
        add(budgetLabel);

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

    public void setCity(City city) {
        this.city = city;
        syncLabels();
    }

    public void syncLabels() {
        if (city == null) return;
        budgetLabel.setText("Expenses: $"+ city.getBudget());
        populationLabel.setText("Population: "+ city.getPopulation());
        satisfactionLabel.setText("Satisfaction: " + city.getSatisfaction() +"%");
        city.timeGone();
        timeLabel.setText(city.getDateTime());
    }
}