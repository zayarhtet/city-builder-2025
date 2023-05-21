package com.coffee.citybuilder.view.component;

import com.coffee.citybuilder.model.zone.ZoneInfo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Class that makes informational dialog when you click a cell
 * 
 * This class gets all the values that are important to the user
 * (Satisfaction/population/ect.) and represents them in a visual way, wih a
 * custom dialog box.
 */
public class InfoDialog extends JDialog {
    private JLabel populationLabel, pensionerCountLabel, satisfactionLabel, electricityLabel, policeLabel,
            employedCountLabel, stadiumLabel;

    public InfoDialog(ZoneInfo zi) {
        setTitle("Information");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Residential Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(titleLabel, gbc);

        int index = 1;

        populationLabel = new JLabel("Total Population: " + zi.population);
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(populationLabel, gbc);

        pensionerCountLabel = new JLabel("Number of Pensioners: " + zi.pensionerCount);
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(pensionerCountLabel, gbc);

        employedCountLabel = new JLabel("Employed Population: " + zi.employedCount);
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(employedCountLabel, gbc);

        satisfactionLabel = new JLabel("Satisfaction: " + zi.satisfaction + "%");
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(satisfactionLabel, gbc);

        electricityLabel = new JLabel("Electricity Access: " + (zi.electricity ? "Yes" : "No"));
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(electricityLabel, gbc);

        stadiumLabel = new JLabel("Stadium Access: " + (zi.stadium ? "Yes" : "No"));
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(stadiumLabel, gbc);

        policeLabel = new JLabel("Police Access: " + (zi.police ? "Yes" : "No"));
        gbc.gridx = 0;
        gbc.gridy = index++;
        contentPanel.add(policeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = index++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
