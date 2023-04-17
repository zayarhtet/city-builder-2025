package model.zone;

import model.Position;

public class Zone {
    private Position location;
    private int numOfPeople;

    public Zone(Position p) {
        location = new Position(p);
    }
}
