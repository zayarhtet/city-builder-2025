package com.coffee.citybuilder.model;

/**
 * Disaster type to deploy on user's command
 */
public enum Disaster {
    Titan("RUN!!!!",1,40,80);

    private final String text;
    public final int width, height;
    public final double speed;

    Disaster(String text,double speed,int width,int height){
        this.text = text;
        this.speed = speed;
        this.width = width;
        this.height =height;
    }
}
