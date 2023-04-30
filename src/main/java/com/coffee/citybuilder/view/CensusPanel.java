package com.coffee.citybuilder.view;

import com.coffee.citybuilder.model.City;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CensusPanel extends JPanel {
    private MainWindow  frame;
    private JButton     backButton;
    private JLabel      budgetLabel;
    public CensusPanel(MainWindow mw) {
        this.frame = mw;
        setPreferredSize(new Dimension(800, 400));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        backButton = new JButton("Go Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Courier", Font.BOLD, 14));
        backButton.setBackground(MainWindow.BG_COLOR);
        backButton.setForeground(Color.BLACK);

        backButton.addActionListener(e -> {
            this.frame.hideCensusPage();
            this.frame.showMapPage();
        });

//        budgetLabel = new JLabel();
//        budgetLabel.setFont(new Font("Courier", Font.BOLD, 16));
//        budgetLabel.setForeground(MainWindow.THEME);
//        budgetLabel.setText("Budget: " + 390);

        buttonPanel.add(backButton);
//        buttonPanel.add(budgetLabel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public  void syncCensus(City city) {

    }
}
