package com.coffee.citybuilder.view;

import com.coffee.citybuilder.model.City;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * this class will show the statistics of the whole city
 */
public class CensusPanel extends JPanel {
    private MainWindow  frame;
    private JButton     backButton;
    private JLabel populationLabel, employedPopulationLabel, vacancyLabel, unemployedPopulationLabel,
            totalBuildingLabel, buildingWithElectricityLabel, budgetLabel, residentialZonesLabel,
            serviceIndustrialZonesLabel, totalRoadLabel, satisfactionLabel, electricityPopulationLabel;

    /**
     * Init method
     * @param mw the frame we want to show on
     */
    public CensusPanel(MainWindow mw) {
        this.frame = mw;
        setPreferredSize(new Dimension(800, 400));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel stat = new JPanel(new GridBagLayout());
        stat.setBorder(BorderFactory.createTitledBorder("Census Information"));
        stat.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 20;
        gbc.ipady = 10;

        Font labelFont = new Font("Arial", Font.BOLD, 16);

        populationLabel = new JLabel("Population: ");
        populationLabel.setFont(labelFont);
        stat.add(populationLabel, gbc);

        employedPopulationLabel = new JLabel("Employed Population: ");
        employedPopulationLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(employedPopulationLabel, gbc);

        vacancyLabel = new JLabel("Vacancy: ");
        vacancyLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(vacancyLabel, gbc);

        unemployedPopulationLabel = new JLabel("Unemployed Population: ");
        unemployedPopulationLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(unemployedPopulationLabel, gbc);

        totalBuildingLabel = new JLabel("Total Building: ");
        totalBuildingLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(totalBuildingLabel, gbc);

        residentialZonesLabel = new JLabel("Residential Zones: ");
        residentialZonesLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(residentialZonesLabel, gbc);

        serviceIndustrialZonesLabel = new JLabel("Service Industrial Zones: ");
        serviceIndustrialZonesLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(serviceIndustrialZonesLabel, gbc);

        totalRoadLabel = new JLabel("Total Road: ");
        totalRoadLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(totalRoadLabel, gbc);

        buildingWithElectricityLabel = new JLabel("Building with Electricity: ");
        buildingWithElectricityLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(buildingWithElectricityLabel, gbc);

        electricityPopulationLabel = new JLabel("Population with Electricity: ");
        electricityPopulationLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(electricityPopulationLabel, gbc);

        satisfactionLabel = new JLabel("Satisfaction: ");
        satisfactionLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(satisfactionLabel, gbc);

        budgetLabel = new JLabel("Budget: ");
        budgetLabel.setFont(labelFont);
        gbc.gridy++;
        stat.add(budgetLabel, gbc);

        add(stat, BorderLayout.CENTER);

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

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the lables accordingly
     * @param city the city
     */
    public void syncCensus(City city) {
        populationLabel.setText("Population: " + city.getPopulation());
        employedPopulationLabel.setText("Employed Population: " + city.getEmployedCount());
        vacancyLabel.setText("Vacancy: " + city.getVacancyCount());
        unemployedPopulationLabel.setText("Unemployed Population: " + city.getUnemployedCount());
        totalBuildingLabel.setText("Total Buildings: " + city.getTotalBuilding());
        buildingWithElectricityLabel.setText("Buildings with Electricity: " + city.getElectricityCount());
        budgetLabel.setText("Budget: " + city.getBudget());
        residentialZonesLabel.setText("Residential Zones: " + city.getResidentialZoneCount());
        serviceIndustrialZonesLabel.setText("Service/Industrial Zones: " + city.getServiceIndustrialZoneCount());
        totalRoadLabel.setText("Total Roads: " + city.getTotalRoad());
        satisfactionLabel.setText("Satisfaction: " + city.getSatisfaction());
        electricityPopulationLabel.setText("Population with Electricity: "+ city.getElectricityPopulation());
        // Set color for satisfaction label
        if (city.getSatisfaction() > 80) {
            satisfactionLabel.setForeground(Color.GREEN);
        } else if (city.getSatisfaction() > 50) {
            satisfactionLabel.setForeground(Color.ORANGE);
        } else {
            satisfactionLabel.setForeground(Color.RED);
        }

        // Set color for unemployment label
        if (city.getUnemployedCount() > 0) {
            unemployedPopulationLabel.setForeground(Color.RED);
        } else {
            unemployedPopulationLabel.setForeground(Color.BLACK);
        }
    }
}
