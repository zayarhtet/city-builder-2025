package com.coffee.citybuilder.view;

import javax.swing.*;

import com.coffee.citybuilder.resource.ResourceLoader;
import com.coffee.citybuilder.view.component.InputDialog;
import com.coffee.citybuilder.view.component.MenuButton;

import java.awt.*;
import java.io.IOException;

/**
 * Main menu panel that display menu items.
 * 
 * This classdraws the menu GUI, by initiaizing buttons, which let the user
 * start a new game, load a previuos game and exit the game.
 */
public class MenuPanel extends JPanel {

    private final Image backgroundImage;
    private final MainWindow frame;

    private int GAP = 100;
    private int BORDER_SIZE = 380;

    public MenuPanel(MainWindow frame) throws IOException {
        this.frame = frame;
        backgroundImage = ResourceLoader.loadImage("resource/intro-bg.png");
        setLayout(new BorderLayout(0, 10));

        JPanel menuTextPanel = new JPanel();
        menuTextPanel.setOpaque(false);

        JButton newGameButton = new MenuButton("New Game");
        newGameButton.addActionListener(e -> {
            String gameName = "";

            InputDialog idialog = new InputDialog();
            gameName = idialog.getInput();
            if (gameName.length() > 0) {
                frame.instantiateGame("", gameName);
                this.frame.hideMenuPage();
                this.frame.showMapPage();
            }
        });

        menuTextPanel.add(newGameButton);
        menuTextPanel.add(Box.createRigidArea(new Dimension(0, GAP)));

        JButton loadGameButton = new MenuButton("Load Game");
        loadGameButton.addActionListener(e -> {
            this.frame.instantiateLoadGame();
            this.frame.hideMenuPage();
            this.frame.showLoadGamePage();
        });
        menuTextPanel.add(loadGameButton);
        menuTextPanel.add(Box.createRigidArea(new Dimension(0, GAP)));
        menuTextPanel.add(new MenuButton("Quit Game"));

        menuTextPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(menuTextPanel, BorderLayout.CENTER);

        // dummy panel for menu alignment
        JPanel dummyPanelWest = new JPanel();
        dummyPanelWest.setPreferredSize(new Dimension(BORDER_SIZE, 0));
        dummyPanelWest.setOpaque(false);
        add(dummyPanelWest, BorderLayout.WEST);

        JPanel dummyPanelEast = new JPanel();
        dummyPanelEast.setPreferredSize(new Dimension(BORDER_SIZE, 0));
        dummyPanelEast.setOpaque(false);
        add(dummyPanelEast, BorderLayout.EAST);

        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(0, 300));

        JLabel title = new JLabel("CityBuilder 2025");
        title.setFont(new Font("Courier", Font.BOLD, 80));
        title.setForeground(new Color(225, 214, 124));

        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        setSize(new Dimension(1000, 698));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
    }
}
