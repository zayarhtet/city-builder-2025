package view;

import model.Disaster;
import model.Position;
import resource.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Animator {
    private Disaster disaster;
    private Position end;
    private String mainPath = "resource/sprites/";
    private Map<String,Image> sprites = new HashMap<>();
    //private String[] keys = new String[]{ "Stand","Move1","Move2","Prep","Attack" };
    private String[] keys = new String[]{"Move1.png","Move2.png"};
    private boolean isAnimating = false;
    private int counter = 0, minDistance = 2;
    private int dirx,diry;
    double speedx,speedy;
    double drawx,drawy;
    public void SetUp(Disaster d, Position end){
        try{
            disaster = d;
            LoadSprites(disaster);
            SetDirection(end);
            SetVelocity(end);
            drawx = 13*30;
            drawy = 8*30;
            this.end = new Position(end.x*30,end.y*30);
            isAnimating = true;

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void LoadSprites(Disaster d) throws IOException {
        //sprites.clear();
        for(int i=0; i<keys.length; i++){
            Image img = ResourceLoader.loadImage(mainPath+d.name()+"/"+keys[i]);
            sprites.put(keys[i],img);
        }
    }

    void SetDirection(Position end){
        // row 22 col 33
        int midx = 8,midy = 13;

        // Start point + offset calculation
        dirx = end.x < midx ? -1 : 1;
        diry = end.y < midy ? -1 : 1;
    }

    void SetVelocity(Position end){

        double multiplier = Math.abs(13 - end.x) / disaster.speed;
        speedy = Math.abs( 8 - end.y) / multiplier;
        speedx = disaster.speed;

    }

    public void Animate(Graphics2D g){
        if(!isAnimating) return;
        counter++;
        int i = 0;
        if(counter < 30){
            i = 0;
        }
        else if(counter < 60){
            i = 1;
        }
        else{ counter = 0; }
        Image img = sprites.get( keys[i] );

        double dist = Math.sqrt( Math.pow(drawx-end.x,2) + Math.pow(drawy-end.y,2) );
        if(dist <= minDistance * 30){
            isAnimating = false;
            mainPath = "resource/sprites/";
        }

        drawx = drawx + (speedx * dirx);
        drawy = drawy + (speedy * diry);

        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
    }

    public boolean isAnimating() { return isAnimating; }
}
