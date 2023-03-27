package view.component;

import model.CellItem;
import view.MainWindow;

import javax.swing.*;
import java.awt.*;

public class InGameButtonPanel extends JPanel {
    private MainWindow frame;
    public InGameButtonPanel(MainWindow frame, Color theme) {
        this.frame = frame;
        setPreferredSize(new Dimension(50,698));
        setBackground(theme);

        JButton roadBtn = new InGameButton("resource/h-road.png", CellItem.H_ROAD);
        add(roadBtn);

        JButton vRoadBtn = new InGameButton("resource/v-road.png", CellItem.V_ROAD);
        add(vRoadBtn);

        JButton botRightRoadBtn = new InGameButton("resource/bot-right-road.png", CellItem.JUNCTION_ROAD);
        add(botRightRoadBtn);

    }
}
