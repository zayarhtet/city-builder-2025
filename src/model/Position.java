package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position)){
            return false;
        }
        Position other = (Position) o;
        return Objects.equals(other.x, this.x) && Objects.equals(other.y, this.y);
    }
}
