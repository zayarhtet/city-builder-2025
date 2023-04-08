package view;

import resource.ResourceLoader;
import view.component.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private final Image     backgroundImage;
    private final MainWindow    frame;

    private int GAP = 100;
    private int BORDER_SIZE = 380;

    public MenuPanel(MainWindow frame) throws IOException {
        this.frame = frame;
        backgroundImage = ResourceLoader.loadImage("resource/intro-bg.png");
        setLayout(new BorderLayout(0,10));

        JPanel menuTextPanel = new JPanel();
        menuTextPanel.setOpaque(false);

        JButton newGameButton = new MenuButton("New Game");
        newGameButton.addActionListener(e -> {
            //TODO dialog box here, should The variable(gameName) be extended?
            //TODO should it be wraped in try catch?
            String gameName = "";
            do {
                gameName = (String) JOptionPane.showInputDialog("New game's name");
                if (gameName == null) {
                    gameName = "";
                    break;
                }
            } while ((gameName.length() == 0));

            if (gameName.length() > 0) {
                frame.instantiateGame("d13c4e3e-bb61-4489-b6ca-d3fb71769e41",gameName);
                this.frame.hideMenuPage();
                this.frame.showMapPage();
            }
        });
        
        menuTextPanel.add(newGameButton);
        menuTextPanel.add(Box.createRigidArea(new Dimension(0, GAP)));
        menuTextPanel.add(new MenuButton("Load Game"));
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

        setSize(new Dimension(1000,698));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0,0,null);
    }
}
