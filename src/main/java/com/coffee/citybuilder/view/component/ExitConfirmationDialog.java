package com.coffee.citybuilder.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.coffee.citybuilder.view.MainWindow;

/**
 * A custom dialog show when user clicks exit.
 * 
 * This custom dialog s shown when the user tries to close
 * the program, it gives him a choice if he wants to exit,
 * thus preventing a user from exiting accidentally.
 */
public class ExitConfirmationDialog extends JDialog implements ActionListener {
    private boolean confirmed = false;

    public ExitConfirmationDialog() {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Exit Confirmation");
        setPreferredSize(new Dimension(350, 150));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create content pane
        JPanel contentPane = new JPanel();
        contentPane.setBackground(MainWindow.BG_COLOR);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Create message label
        TextLabel messageLabel = new TextLabel("Are you sure you want to exit?");
        messageLabel.setFont(new Font("Courier", Font.BOLD, 16));
        messageLabel.setForeground(MainWindow.THEME);
        messageLabel.setBackground(MainWindow.BG_COLOR);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        contentPane.add(messageLabel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(MainWindow.BG_COLOR);
        buttonPanel.setLayout(new BorderLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Create cancel button
        JButton cancelButton = new JButton("No");
        cancelButton.addActionListener(this);
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(225, 124, 135));
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(130, 0, 0)));
        buttonPanel.add(cancelButton, BorderLayout.WEST);

        // Create OK button
        JButton okButton = new JButton("Yes");
        okButton.addActionListener(this);
        okButton.setPreferredSize(new Dimension(100, 30));
        okButton.setForeground(Color.BLACK);
        okButton.setBackground(new Color(135, 225, 124));
        okButton.setBorder(BorderFactory.createLineBorder(new Color(0, 130, 0)));
        buttonPanel.add(okButton, BorderLayout.EAST);

        // Pack and center dialog
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Returns ture if the user clicked he is sure he
     * wants to exit.
     * 
     * @return boolean
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Used to overwrite base behaviour so that we can catch clicks and close the
     * game.
     * 
     * @param e - ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Yes")) {
            confirmed = true;
        }
        dispose();
    }

    /**
     * Custom Text Label class so that it fits in nicely with other components.
     */
    private static class TextLabel extends JPanel {
        private String text;

        public TextLabel(String text) {
            this.text = text;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
            g.setFont(getFont());
            g.drawString(text, 0, getHeight() - 5);
        }
    }
}
