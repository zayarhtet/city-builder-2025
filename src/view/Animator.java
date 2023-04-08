package view;

import model.Disaster;
import model.Position;
import resource.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Animator {
    private Disaster disaster;
    private Position start,end,current;
    private String mainPath = "resource/sprites/";
    private Map<String,Image> sprites = new HashMap<>();
    //private String[] keys = new String[]{ "Stand","Move1","Move2","Prep","Attack" };
    private String[] keys = new String[]{"Move1.png","Move2.png"};
    private boolean isAnimating = false;
    private int counter = 0;
    public void SetUp(Disaster d,Position start, Position end){
        try{
            disaster = d;
            LoadSprites(disaster);
            this.start = start;
            this.current = new Position(start);
            this.end = end;
            System.out.println("To "+end.x+" "+end.y);
            isAnimating = true;

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println(sprites.size());
    }

    private void LoadSprites(Disaster d) throws IOException {
        //sprites.clear();
        for(int i=0; i<keys.length; i++){
            System.out.println(mainPath+keys[i]);
            //InputStream istream = ResourceLoader.loadResource(mainPath+keys[i]);
            //BufferedImage img = ImageIO.read(istream);
            Image img = ResourceLoader.loadImage(mainPath+d.name()+"/"+keys[i]);
            sprites.put(keys[i],img);
        }
    }

    public void Animate(Graphics2D g){
        if(!isAnimating) return;
        counter++;
        int i = 0;
        Image img = sprites.get( keys[i] );
        if(counter >= 120){
            i = 1;
            counter = 0;
        }

        double dist = Math.sqrt( Math.pow(current.x-end.x,2) + Math.pow(current.y-end.y,2) );
        if(dist <= 5){
            isAnimating = false;
            mainPath = "resource/sprites/";
        }

        current = new Position(current.x+disaster.speed,current.y+ disaster.speed);
        g.drawImage(sprites.get(keys[i]),current.x, current.y,30,30,null);
    }

    public boolean isAnimating() { return isAnimating; }
}
