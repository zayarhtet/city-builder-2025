package view;

import model.Game;
import view.component.EventModel;
import view.component.InGameButtonPanel;
import view.component.StatisticPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {
    private MenuPanel   menu;
    private StatisticPanel      statisticPanel;
    private InGameButtonPanel   buttonPanel;
    private CityMap     map;
    private int         width       = 1000,
                        height      = 698;
    private Color       theme       = new Color(126,121,224,255);
    private Game        game;
    private Timer       timer;
    public MainWindow() {
        setTitle("CityBuilder 2025");
        setSize(width, height);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.saveCities();
                System.exit(0);
            }
        });
        URL url = MainWindow.class.getClassLoader().getResource("resource/player.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        setLayout(new BorderLayout(0, 2));

        // theme
        Color menuButtonColor = new Color(0, 90, 255);
        this.game = new Game();

        try { add(menu = new MenuPanel(this), BorderLayout.CENTER); } catch (IOException e) {}

        this.statisticPanel = new StatisticPanel(this, theme);
        add(statisticPanel, BorderLayout.NORTH);
        statisticPanel.setVisible(false);

        this.buttonPanel = new InGameButtonPanel(this, theme);
        add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setVisible(false);

        setPreferredSize(new Dimension(width,height));
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public void hideMenuPage() { menu.setVisible(false); }
    public void showMenuPage() { menu.setVisible(true); }
    public void hideMapPage() { map.setVisible(false); statisticPanel.setVisible(false); buttonPanel.setVisible(false); }
    public void showMapPage() { map.setVisible(true); statisticPanel.setVisible(true); buttonPanel.setVisible(true);}
    public void instantiateGame(String id, String username) {
        if (map == null) {
            try { add(map = new CityMap(this), BorderLayout.CENTER); } catch (IOException e) {}
        }
        map.setCity(game.loadCity(id, username));

        map.initiateRandomVehicles(1);
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!EventModel.isGenuinelyFree()) return;

                map.repaint();
            }
        });
        timer.start();
    }
}
