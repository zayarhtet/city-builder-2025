package com.coffee.citybuilder.view;

import com.coffee.citybuilder.model.budget.Bank;
import com.coffee.citybuilder.model.budget.Transaction;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * this class will show the purchases and reimbursement made
 */
public class TransactionPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> dropdown;
    private JButton backButton;
    private JLabel budgetLabel;
    private MainWindow frame;

    private Bank bank;

    /**
     * Init method
     * @param bank money storage class of our city
     * @param mw frame we want to show on
     */
    public TransactionPanel(Bank bank, MainWindow mw) {
        this.bank = bank; this.frame = mw;

        setPreferredSize(new Dimension(800, 400));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Datetime", "Reason", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Courier", Font.BOLD, 16));
        table.setFont(new Font("Courier", Font.PLAIN, 14));
        table.setBackground(MainWindow.BG_COLOR); table.setForeground(Color.BLACK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        String[] dropdownItems = {"Expenses", "Incomes"};
        dropdown = new JComboBox<>(dropdownItems);
        dropdown.setPreferredSize(new Dimension(100,40));
        dropdown.setFont(new Font("Courier", Font.BOLD, 14));
        dropdown.setBackground(MainWindow.THEME);dropdown.setForeground(Color.WHITE);
        dropdown.addActionListener(e -> syncTable(this.bank));

        backButton = new JButton("Go Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Courier", Font.BOLD, 14));
        backButton.setBackground(MainWindow.BG_COLOR);
        backButton.setForeground(Color.BLACK);

        backButton.addActionListener(e -> {
            this.frame.hideTransactionPage();
            this.frame.showMapPage();
        });

        budgetLabel = new JLabel();
        budgetLabel.setFont(new Font("Courier", Font.BOLD, 16));
        budgetLabel.setForeground(MainWindow.THEME);
        budgetLabel.setText("Budget: " + this.bank.getBudget());

        buttonPanel.add(backButton);
        buttonPanel.add(dropdown);
        buttonPanel.add(budgetLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        syncTable(this.bank);
        setVisible(false);
    }

    /**
     * set the content of the table to the bank's transactions
     * @param bank
     */
    public void syncTable(Bank bank) {
        this.bank = bank;
        tableModel.setRowCount(0);

        List<Transaction> selectedData;
        if (dropdown.getSelectedItem().equals("Expenses")) {
            selectedData = this.bank.getExpenses();
        } else {
            selectedData = this.bank.getIncomes();
        }

        for (Transaction transaction : selectedData) {
            Object[] rowData = {transaction.getDatetime(), transaction.getReason(), transaction.getAmount()};
            tableModel.addRow(rowData);
        }

        budgetLabel.setText("Budget: " + this.bank.getBudget());
    }
}
