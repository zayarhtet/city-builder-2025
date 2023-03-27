package view;

import view.component.EventModel;
import view.component.InGameButtonPanel;
import view.component.StatisticPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {
    private MenuPanel   menu;
    private CityMap     map;
    private int         width       = 1000,
                        height      = 698;
    private Color       theme       = new Color(126,121,224,255);

    public MainWindow() {
        setTitle("CityBuilder 2025");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("resource/player.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        setLayout(new BorderLayout(0, 2));

        // theme
        Color menuButtonColor = new Color(0, 90, 255);

        try { add(menu = new MenuPanel(this), BorderLayout.CENTER); } catch (IOException e) {}
        try { add(map = new CityMap(this), BorderLayout.CENTER); } catch (IOException e) {}


        JPanel statisticPanel = new StatisticPanel(theme);
        add(statisticPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new InGameButtonPanel(this, theme);
        add(buttonPanel, BorderLayout.EAST);

        setPreferredSize(new Dimension(width,height));
        System.out.println(getSize());
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public void hideMenuPage() { menu.setVisible(false); }
    public void showMenuPage() { menu.setVisible(true); }
    public void hideMapPage() { map.setVisible(false); }
    public void showMapPage() { map.setVisible(true); }

}
