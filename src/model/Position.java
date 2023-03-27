package model;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position original) {
        this.x = original.x;
        this.y = original.y;
    }
}
