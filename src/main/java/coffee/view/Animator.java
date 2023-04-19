package coffee.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coffee.model.Disaster;
import coffee.model.Position;
import coffee.resource.ResourceLoader;

import java.lang.Math;

public class Animator {
    private Disaster disaster;
    private Position end;
    private String mainPath = "resource/sprites/";
    private Map<String,Image> sprites = new HashMap<>();
    private String[] keys = new String[]{
     //"Idle",
     "Spawn1","Spawn2","Spawn3","Spawn4","Spawn5",
     "Move1","Move2","Move3",
     "Prep1","Prep2","Prep3",
     "Attack1","Attack2","Attack3" };

    private boolean isAnimating = false, canInterrupt = true;
    private int counter = 0, minDistance = 1, step = 0, imgIndex = 1;
    private int dirx,diry;
    private double speedx,speedy;
    private double drawx,drawy;
    private int FPS = 45;
    private AnimState state = AnimState.SPAWN;

    public void SetUp(Disaster d, Position endP){
        endP = new Position(endP.y,endP.x);

        try{
            disaster = d;
            LoadSprites(disaster);
            SetDirection(endP);
            SetVelocity(new Position(14,10),endP);
            drawx = 14*30;
            drawy = 8*30;
            endP.x--; endP.y-=2;
            this.end = new Position(endP.x*30,endP.y*30);
            isAnimating = true;
            counter = step = 0;
            imgIndex = 1;
            this.state = AnimState.SPAWN;
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void LoadSprites(Disaster d) throws IOException {
        //sprites.clear();
        for(int i=0; i<keys.length; i++){
            Image img = ResourceLoader.loadImage(mainPath+d.name()+"/"+keys[i]+".png");
            sprites.put(keys[i],img);
        }
    }

    void SetDirection(Position end){
        // row 22 col 33
        int midx = 13,midy = 8;

        // Start point + offset calculation
        dirx = end.x < midx ? -1 : 1;
        diry = end.y < midy ? -1 : 1;

        if(end.x == midx){
            dirx = 0;
        }
        if(end.y == midy){
            diry = 0;
        }
    }

    void SetVelocity(Position start,Position end){

        double multiplier = Math.abs(start.x - end.x) / disaster.speed;
        speedy = Math.abs(start.y - end.y) / multiplier;
        speedx = disaster.speed;
    }

    public void Animate(Graphics2D g){
        if(!isAnimating) {
            return;
        }

        if(drawx < -30 || drawx > 35*30){
            isAnimating = false;
            return;
        }
        if(drawy< -30 || drawy > 25*30){
            isAnimating = false;
            return;
        }

        ChangeState();

        switch (this.state){

            case SPAWN:
                SpawnAnim(g); break;
            case MOVE:
                MoveAnim(g); break;
            case ATTACK:
                AttackAnim(g); break;
            default:
                break;
        }




    }

    void ChangeState(){


        if(!canInterrupt){
            if(this.state == AnimState.MOVE){
                double dist = Math.sqrt( Math.pow(drawx-end.x,2) + Math.pow(drawy-end.y,2) );
                if(dist <= minDistance * 30){
                    this.state = AnimState.ATTACK;
                    step = 0;
                    imgIndex = 1;
                    canInterrupt = true;
                }
            }
            return;
        }


        counter++;
        if(counter/FPS >= 2){
            if(this.state == AnimState.ATTACK){
                isAnimating = false;
                return;
            }
            this.state  = AnimState.values()[this.state.ordinal()+1];
            if(this.state == AnimState.MOVE){
                canInterrupt = false;
            }
            counter = 0;
            step = 0;
            imgIndex = 1;
        }

    }

    void SpawnAnim(Graphics2D g){
        step++;
        if(step > (2 * FPS) / 5){
            if(imgIndex < 5 )
                imgIndex = ( imgIndex+1 ) % 6;
            step = 0;
        }
        String key = "Spawn" + imgIndex;
        Image img = sprites.get(key);
        if(img == null) return;
        g.drawImage(img,AffineTransform.getTranslateInstance(drawx-60,drawy-90),null);
    }

    void MoveAnim(Graphics2D g){

        step++;
        if(step > (2 * FPS) / 3){
            imgIndex = ( imgIndex+1 ) % 4;
            imgIndex++;
            step = 0;
        }
        String key = "Move" + imgIndex;
        Image img = sprites.get( key );

        drawx = drawx + (speedx * dirx);
        drawy = drawy + (speedy * diry);
        if(dirx * diry == 0){
            g.drawImage(img,(int)drawx,(int)drawy,null);
            return;
        }

        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
    }

    void PrepAnim(Graphics2D g){
        step++;
        if(step > (2 * FPS) / 3){
            imgIndex = ( imgIndex+1 ) % 4 ;
            imgIndex++;
            step = 0;
        }
        String key = "Prep" + imgIndex;
        Image img = sprites.get( key );

        drawx = drawx + (speedx * dirx);
        drawy = drawy + (speedy * diry);
        if(img == null) return;
        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
    }

    void AttackAnim(Graphics2D g){
        step++;
        if(step >= FPS ){
            if(imgIndex < 3)
                imgIndex++;
        }
        String key = "Attack"+imgIndex;
        Image img = sprites.get(key);
        if(img == null) return;
        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
    }

    public boolean isAnimating() { return isAnimating; }
}

enum AnimState {
    SPAWN,MOVE, ATTACK
}
