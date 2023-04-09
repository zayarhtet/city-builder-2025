package view;

import model.Disaster;
import model.Position;
import resource.ResourceLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
            //System.out.println(mainPath+d.name()+"/"+keys[i]);
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
    }

    void SetVelocity(Position start,Position end){

        double multiplier = Math.abs(start.x - end.x) / disaster.speed;
        speedy = Math.abs(start.y - end.y) / multiplier;
        speedx = disaster.speed;
    }

    public void Animate(Graphics2D g){
        if(!isAnimating) {
            //g.clearRect(0,0,23*30,34*30);
            return;
        }

        // Spawn:2, Move:Until Target, Prep:2, Attack:2
        ChangeState();

        switch (this.state){

            case SPAWN:
                SpawnAnim(g); break;
            //case IDLE:
            //    ShowText(g); break;
            case MOVE:
                MoveAnim(g); break;
            //case PREP:
            //    PrepAnim(g); break;
            case ATTACK:
                //System.out.println("Attacking "+counter+" "+step);
                AttackAnim(g); break;
            default:
                break;
        }




    }

    void ChangeState(){


        if(!canInterrupt){
            //System.out.println("Calc state is "+this.state);
            if(this.state == AnimState.MOVE){
                double dist = Math.sqrt( Math.pow(drawx-end.x,2) + Math.pow(drawy-end.y,2) );
                //System.out.println(dist);
                if(dist <= minDistance * 30){
                    //this.state  = AnimState.PREP;
                    this.state = AnimState.ATTACK;
                    step = 0;
                    imgIndex = 1;
                    canInterrupt = true;
                    //SetVelocity(new Position((int)drawx,(int)drawy),end);
                }
            }
//            else if(this.state == AnimState.PREP){
//                minDistance = 1;
//                double dist = Math.sqrt( Math.pow(drawx-end.x,2) + Math.pow(drawy-end.y,2) );
//                if(dist <= minDistance * 30){
//                    canInterrupt = true;
//                    this.state  = AnimState.ATTACK;
//                    step = 0;
//                    imgIndex = 1;
//                }
//
//            }
            return;
        }


        counter++;
        if(counter/FPS >= 2){
            System.out.println(this.state.name());
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
        if(img == null) {
            //System.out.println("Null "+imgIndex);
            return;
        }
        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
        //AffineTransform t = new AffineTransform(1.5,3.0,0,0,drawx,drawy);
        //g.drawImage(img,t,null);
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
        //AffineTransform t = new AffineTransform(1.5,3.0,0,0,drawx,drawy);
        //g.drawImage(img,t,null);
    }

    void AttackAnim(Graphics2D g){
        step++;
        if(step >= FPS ){
            if(imgIndex < 3)
                imgIndex++;
        }
        String key = "Attack"+imgIndex;
        Image img = sprites.get(key);
        //AffineTransform t = new AffineTransform(1.5,3.0,0,0,drawx,drawy);
        if(img == null) return;
        g.drawImage(img,AffineTransform.getTranslateInstance(drawx,drawy),null);
        //g.drawImage(img,t,null);
    }

    public boolean isAnimating() { return isAnimating; }
}

enum AnimState {
    SPAWN,MOVE,
    //PREP,
    ATTACK
}
