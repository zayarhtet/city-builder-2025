package coffee.model;

public enum Disaster {
    Titan("RUN!!!!",1,40,80),
    Storm("HEAVY RAIN",1, 40, 40);

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
