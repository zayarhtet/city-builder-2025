package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public enum Disaster {
    Titan("RUN!!!!",2,40,80),
    Storm("HEAVY RAIN",1, 40, 40);

    private final String text;
    public final int speed, width, height;

    Disaster(String text,int speed,int width,int height){
        this.text = text;
        this.speed = speed;
        this.width = width;
        this.height =height;
    }
}
