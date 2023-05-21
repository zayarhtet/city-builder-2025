package com.coffee.citybuilder.view;

import javax.swing.*;

import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.model.Game;
import com.coffee.citybuilder.model.budget.Bank;
import com.coffee.citybuilder.view.component.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {
    private MenuPanel           menu;
    private LoadGamePanel       loadGamePanel;
    private TransactionPanel    transactionPanel;
    private CensusPanel         censusPanel;
    private StatisticPanel      statisticPanel;
    private InGameButtonPanel   buttonPanel;
    private CityMap             map;
    private int                 width       = 1000,
                                height      = 698;
    public static Color         THEME       = new Color(126,121,224,255);
    public static Color         BG_COLOR           = new Color(225, 214, 124);
    private Game                game;
    private Timer               inGameTimer;
    public MainWindow() {
        setTitle("CityBuilder 2025");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                inGameTimer.stop();
                ExitConfirmationDialog dialog = new ExitConfirmationDialog();
                if (dialog.isConfirmed()) {
                    game.saveCities();
                    dispose();
                    System.exit(0);
                }
                inGameTimer.start();
            }
        });
        String path = "resource/player.png";
        URL url = MainWindow.class.getClassLoader().getResource(path);
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        setLayout(new BorderLayout(0, 2));

        this.game = new Game();

        try { add(menu = new MenuPanel(this), BorderLayout.CENTER); } catch (IOException e) {}

        this.statisticPanel = new StatisticPanel(this);
        add(statisticPanel, BorderLayout.NORTH);
        statisticPanel.setVisible(false);

        this.buttonPanel = new InGameButtonPanel(this, THEME);
        add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setVisible(false);

        this.inGameTimer = new Timer(500, e -> {
            this.statisticPanel.syncLabels();
        });
        inGameTimer.start();
        setPreferredSize(new Dimension(width,height));
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * Double Speed
     */
    public void doubleSpeed() {
        inGameTimer.stop();
        inGameTimer.setDelay(50);
        map.setVehicleFrameRate(2);
        inGameTimer.start();
    }

    /**
     * Normal Speed
     */
    public void normalSpeed() {
        inGameTimer.stop();
        inGameTimer.setDelay(500);
        map.setVehicleFrameRate(1);
        inGameTimer.start();
    }
    public void hideMenuPage() { menu.setVisible(false); }
    public void showMenuPage() { menu.setVisible(true); }
    public void hideMapPage() { map.setVisible(false); statisticPanel.setVisible(false); buttonPanel.setVisible(false); inGameTimer.stop(); }
    public void showMapPage() { map.setVisible(true); statisticPanel.setVisible(true); buttonPanel.setVisible(true); inGameTimer.start(); }
    public void showLoadGamePage() { loadGamePanel.setVisible(true); }
    public void hideLoadGamePage() { loadGamePanel.setVisible(false); }
    public void showTransactionPage(Bank bank) {
        if (transactionPanel == null) {
            add(this.transactionPanel = new TransactionPanel(bank, this), BorderLayout.CENTER);
        }
        transactionPanel.syncTable(bank);
        transactionPanel.setVisible(true);
    }

    /**
     * Initialize the Census Page
     * @param city
     */
    public void showCensusPage(City city) {
        if (censusPanel == null) {
            add(this.censusPanel = new CensusPanel(this), BorderLayout.CENTER);
        }
        censusPanel.syncCensus(city);
        censusPanel.setVisible(true);
    }
    public void hideTransactionPage() {
        transactionPanel.setVisible(false);
    }
    public void hideCensusPage() {
        censusPanel.setVisible(false);
    }

    /**
     * Initialize the LoadGame Panel
     */
    public void instantiateLoadGame() {
        if (loadGamePanel == null) {
            add(loadGamePanel = new LoadGamePanel(this.game.getAllCities(),  this));
        }
        loadGamePanel.syncTable();
    }

    /**
     * Initialize the game
     * @param id
     * @param username
     */
    public void instantiateGame(String id, String username) {
        if (map == null) {
            try { add(map = new CityMap(this), BorderLayout.CENTER); } catch (IOException e) {}
        }
        City city = game.loadCity(id, username);
        map.setCity(city);
        statisticPanel.setCity(city);

        map.initiateRandomVehicles(1);
    }
}
