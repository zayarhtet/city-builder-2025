package com.coffee.citybuilder.view.component;

import javax.swing.*;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.view.MainWindow;

import java.awt.*;

/**
 * this class is to handle the buttons for in-game activities such as
 * assigning zones, constructing buildings and roads, deleting, starting disasters
 */
public class InGameButtonPanel extends JPanel {
    private MainWindow frame;
    public InGameButtonPanel(MainWindow frame, Color theme) {
        this.frame = frame;
        setPreferredSize(new Dimension(50,698));
        setBackground(theme);

        JButton residentBtn = new InGameButton("resource/residential.png",CellItem.RESIDENTIAL);
        add(residentBtn);

        JButton industrialBtn = new InGameButton("resource/factory.png",CellItem.SERVICE_INDUSTRIAL);
        add(industrialBtn);

        JButton policeDepartmentBtn = new InGameButton("resource/pd-2.png",CellItem.POLICE_DEPARTMENT);
        add(policeDepartmentBtn);

        JButton stadiumBtn = new InGameButton("resource/stadium.png",CellItem.STADIUM);
        add(stadiumBtn);

        JButton ppBtn = new InGameButton("resource/powerplant.png",CellItem.POWER_PLANT);
        add(ppBtn);

        JButton roadBtn = new InGameButton("resource/h-road.png", CellItem.H_ROAD);
        add(roadBtn);

        JButton vRoadBtn = new InGameButton("resource/v-road.png", CellItem.V_ROAD);
        add(vRoadBtn);

        JButton botRightRoadBtn = new InGameButton("resource/bot-right-road.png", CellItem.JUNCTION_ROAD);
        add(botRightRoadBtn);

        JButton transmissionBtn = new InGameButton("resource/Trans.png", CellItem.TRANSMISSION_LINE);
        add(transmissionBtn);

        JButton disasterBtn = new InGameButton("resource/disaster.png",CellItem.DISASTER);
        add(disasterBtn);

        JButton deleteBtn = new InGameButton("resource/delete.png",CellItem.DEL_OPT);
        add(deleteBtn);

    }
}
