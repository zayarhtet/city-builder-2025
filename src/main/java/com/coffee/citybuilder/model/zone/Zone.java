package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

public class Zone {
    private Position location;
    protected int numOfPeople;
    protected CellItem ct;
    public Zone(Position p) {
        location = new Position(p);
    }

    public CellItem getCt() { return ct; }
    public boolean isAt(Position p) { return this.location.equals(p); }
    public int getPopulation() { return this.numOfPeople; }
}
