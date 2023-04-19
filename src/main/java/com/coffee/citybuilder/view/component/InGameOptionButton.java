package com.coffee.citybuilder.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InGameOptionButton extends InGameButton{
    public InGameOptionButton (String path) {
        super(path,null);

        setFont(new Font("Courier", Font.BOLD, 10));
        setForeground(new Color(225, 214, 124));

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(super.isSelected()){

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
