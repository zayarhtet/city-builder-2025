package view.component;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {
    public MenuButton (String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);

        setFont(new Font("Courier", Font.BOLD, 40));
        setForeground(new Color(225, 214, 124));

    }
}
