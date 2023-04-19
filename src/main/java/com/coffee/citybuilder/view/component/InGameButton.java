package com.coffee.citybuilder.view.component;

import jdk.jfr.Event;

import javax.swing.*;

import com.coffee.citybuilder.model.CellItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class InGameButton extends JButton implements MouseListener {
    private final Image element;
    private final String imgPath;
    private final CellItem cell;
    private boolean isSelected = false;

    private Color neutral = new Color(184,207,229,255);
    private Color selectedColor = new Color(225, 214, 124);

    public InGameButton(String path, CellItem cell) {
        super();
        this.imgPath = path;
        this.cell = cell;
        URL url = InGameButtonPanel.class.getClassLoader().getResource(this.imgPath);
        element = Toolkit.getDefaultToolkit().getImage(url);
        Icon icon = new ImageIcon(element.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ));

        setContentAreaFilled(false);
        setIcon(icon);
        setPreferredSize(new Dimension(50,50));

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isSelected) {
            EventModel.DeleteInstance();
            setContentAreaFilled(false);
            isSelected = false;
            return;
        }
        if (!EventModel.isGenuinelyFree()) return;
        EventModel em = EventModel.EventModelInstance(cell);
        setContentAreaFilled(true);
        setBackground(selectedColor);
        isSelected = true;
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {  }

    @Override
    public void mouseExited(MouseEvent e) {  }
}
