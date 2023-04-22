package com.coffee.citybuilder.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.coffee.citybuilder.model.City;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoadGamePanel extends JPanel {
    private final MainWindow frame;
    private JTable table;
    private DefaultTableModel tableModel;

    // List<String> namesList = personList.stream()
    //                                   .map(Person::getName)
    //                                   .collect(Collectors.toList());

    private final List<City> cities;
    public LoadGamePanel(List<City> tCities, MainWindow m) {
        this.frame = m;
        this.cities = tCities;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Set up table
        String[] columnNames = {"Username", "Created Date", "Last Modified Date"};
        Object[][] rowData = new Object[cities.size()][3];
        for (int i = 0; i < cities.size(); i++) {
            rowData[i][0] = cities.get(i).getUsername();
            rowData[i][1] = cities.get(i).getCreatedDate();
            rowData[i][2] = cities.get(i).getModifiedDate();
        }
        tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Courier", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Courier", Font.BOLD, 16));
        table.setBackground(MainWindow.BG_COLOR); table.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Set up buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton loadButton = new JButton("Load");
        loadButton.setPreferredSize(new Dimension(100, 40));
        loadButton.setFont(new Font("Courier", Font.BOLD, 14));
        loadButton.setBackground(new Color(135, 225, 124));
        loadButton.setForeground(Color.BLACK);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 40));
        deleteButton.setFont(new Font("Courier", Font.BOLD, 14));
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.BLACK);

        JButton backToMenuButton = new JButton("Go Back");
        backToMenuButton.setPreferredSize(new Dimension(100, 40));
        backToMenuButton.setFont(new Font("Courier", Font.BOLD, 14));
        backToMenuButton.setBackground(MainWindow.BG_COLOR);
        backToMenuButton.setForeground(Color.BLACK);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    frame.instantiateGame(cities.get(selectedRow).getId(), cities.get(selectedRow).getUsername());
                    frame.hideLoadGamePage();
                    frame.showMapPage();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(LoadGamePanel.this, "Are you sure you want to delete this game?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cities.remove(selectedRow); // fix, should be handled by the backend
                        tableModel.removeRow(selectedRow);
                    }
                }
            }
        });
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.hideLoadGamePage();
                m.showMenuPage();
            }
        });
        buttonPanel.add(backToMenuButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(false);
    }

    public void syncTable() {
        tableModel.setRowCount(0);
        Object[] rowData = new Object[3];
        for (int i = 0; i < cities.size(); i++) {
            rowData[0] = cities.get(i).getUsername();
            rowData[1] = cities.get(i).getCreatedDate();
            rowData[2] = cities.get(i).getModifiedDate();
            tableModel.addRow(rowData);
        }
    }
}