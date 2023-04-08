package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public enum Disaster {
    Titan("RUN!!!!",1),
    Storm("HEAVY RAIN",1);

    private final String text;
    public final int speed;
    Disaster(String text,int speed){
        this.text = text;
        this.speed = speed;
    }
}
