package com.coffee.citybuilder.view.component;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.view.MainWindow;

public class StatisticPanel extends JPanel {

    private JLabel budgetLabel, satisfactionLabel, populationLabel, timeLabel;
    private JButton backToMenuButton, transactionButton, speedBtn, censusButton;
    private final MainWindow frame;
    private City city;
    private boolean isOn = false;

    public StatisticPanel(MainWindow frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(1000, 50));
        setBackground(MainWindow.THEME);
        setLayout(null);

        budgetLabel = new JLabel("$0");
        budgetLabel.setForeground(MainWindow.BG_COLOR);
        budgetLabel.setFont(new Font("Arial", Font.BOLD, 16));
        budgetLabel.setBounds(600, 10, 50, 30);
        add(budgetLabel);

        satisfactionLabel = new JLabel("0%");
        satisfactionLabel.setForeground(MainWindow.BG_COLOR);
        satisfactionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        satisfactionLabel.setBounds(675, 10, 50, 30);
        add(satisfactionLabel);

        populationLabel = new JLabel("0");
        populationLabel.setForeground(MainWindow.BG_COLOR);
        populationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        populationLabel.setBounds(750, 10, 50, 30);
        add(populationLabel);

        timeLabel = new JLabel("2023.04.11 22:10");
        timeLabel.setForeground(MainWindow.BG_COLOR);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(800, 10, 150, 30);
        add(timeLabel);

        backToMenuButton = new JButton();
        backToMenuButton.setFont(new Font("Courier", Font.BOLD, 14));
        backToMenuButton.setBounds(25, 5, 50, 40);
        backToMenuButton.setBackground(MainWindow.BG_COLOR);
        backToMenuButton.setForeground(MainWindow.THEME);

        URL url = InGameButtonPanel.class.getClassLoader().getResource("resource/back-2.png");
        Image element = Toolkit.getDefaultToolkit().getImage(url);
        Icon icon = new ImageIcon(element.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ));
        backToMenuButton.setContentAreaFilled(false);
        backToMenuButton.setIcon(icon);

        add(backToMenuButton);
        backToMenuButton.addActionListener(e -> {
            this.frame.showMenuPage();
            this.frame.hideMapPage();
            EventModel.DeleteInstance();
        });

        transactionButton = new JButton();
        transactionButton.setFont(new Font("Courier", Font.BOLD, 14));
        transactionButton.setBounds(100,5,50,40);
        transactionButton.setBackground(MainWindow.BG_COLOR);
        transactionButton.setForeground(MainWindow.THEME);

        url = InGameButtonPanel.class.getClassLoader().getResource("resource/banking.png");
        element = Toolkit.getDefaultToolkit().getImage(url);
        icon = new ImageIcon(element.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ));
        transactionButton.setContentAreaFilled(false);
        transactionButton.setIcon(icon);

        add(transactionButton);
        transactionButton.addActionListener(e -> {
            this.frame.showTransactionPage(city.getBank());
            this.frame.hideMapPage();
            EventModel.DeleteInstance();
        });

        censusButton = new JButton();
        censusButton.setFont(new Font("Courier", Font.BOLD, 14));
        censusButton.setBounds(175,5,50,40);
        censusButton.setBackground(MainWindow.BG_COLOR);
        censusButton.setForeground(MainWindow.THEME);

        url = InGameButtonPanel.class.getClassLoader().getResource("resource/banking.png");
        element = Toolkit.getDefaultToolkit().getImage(url);
        icon = new ImageIcon(element.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ));
        censusButton.setContentAreaFilled(false);
        censusButton.setIcon(icon);

        add(censusButton);
        censusButton.addActionListener(e -> {
            this.frame.showCensusPage(city);
            this.frame.hideMapPage();
            EventModel.DeleteInstance();
        });

        speedBtn = new JButton();
        speedBtn.setFont(new Font("Courier", Font.BOLD, 14));
        speedBtn.setBounds(250,5,50,40);
        speedBtn.setBackground(MainWindow.BG_COLOR);
        speedBtn.setForeground(MainWindow.THEME);

        url = InGameButtonPanel.class.getClassLoader().getResource("resource/banking.png");
        element = Toolkit.getDefaultToolkit().getImage(url);
        icon = new ImageIcon(element.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ));
        speedBtn.setContentAreaFilled(false);
        speedBtn.setIcon(icon);

        add(speedBtn);
        speedBtn.addActionListener(e -> {
            if (!isOn) {
                this.frame.doubleSpeed();
                speedBtn.setContentAreaFilled(true);
                isOn = true;
            } else {
                this.frame.normalSpeed();
                speedBtn.setContentAreaFilled(false);
                isOn = false;
            }
        });
    }

    public void setCity(City city) {
        this.city = city;
        syncLabels();
    }

    public void syncLabels() {
        if (city == null) return;
        budgetLabel.setText("$"+ city.getBudget());
        populationLabel.setText("P"+ city.getPopulation());
        satisfactionLabel.setText("S" + city.getSatisfaction() +"%");
        city.timeGone();
        timeLabel.setText(city.getDateTime());
    }
}