package view.component;

import com.sun.tools.javac.Main;
import view.MainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputDialog extends JDialog {

    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;
    private boolean inputValid;

    public InputDialog() {
        super();
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Name");
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean isInputValid() {
        return inputValid;
    }

    public String getInput() {
        return inputValid? textField.getText() : "";
    }

    private void initComponents() {
        JPanel contentPane = new JPanel();
        contentPane.setBackground(MainWindow.BG_COLOR);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        getContentPane().add(contentPane);

        // Label
        JLabel label = new JLabel("Please enter your name:");
        label.setFont(new Font("Courier", Font.BOLD, 16));
        label.setForeground(MainWindow.THEME);
        contentPane.add(label, BorderLayout.NORTH);

        // Text field
        textField = new JTextField();
        textField.setForeground(MainWindow.THEME);
        textField.setFont(new Font("Courier", Font.BOLD, 16));
        textField.setPreferredSize(new Dimension(400, 30));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Buttons
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        okButton.setForeground(Color.BLACK);
        okButton.setBackground(new Color(135, 225, 124));
        okButton.setBorder(BorderFactory.createLineBorder(new Color(0, 130, 0)));
        okButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(225, 124, 135));
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(130, 0, 0)));
        cancelButton.setPreferredSize(new Dimension(80, 30));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(MainWindow.BG_COLOR);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        buttonPanel.add(okButton, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        buttonPanel.add(cancelButton, gbc);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(MainWindow.BG_COLOR);
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPane.add(inputPanel, BorderLayout.CENTER);

        // Add action listeners
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText().trim();
                if (input.isEmpty()) {
                    return;
                }
                inputValid = true;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputValid = false;
                dispose();
            }
        });
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        // Draw your modern UI here
//    }

}