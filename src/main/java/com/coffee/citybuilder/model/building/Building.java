package com.coffee.citybuilder.model.building;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

public class Building {
    //private final int cost;
    //private final int reimbursement;
    private List<Building> connections;
    private List<Position> location;
    protected CellItem ct;
    public Building(List<Position> locations){
        this.location = new ArrayList<>(locations);
    }

    public Position topLeft(){
        return location.get(0);
    }

    public boolean contains(Position d) {
        return location.contains(d);
    }

    public List<Position> getLocation() {
        return new ArrayList<>(location);
    }

    public int getPrice() { return ct.price; }

    public CellItem getCt() { return ct; }
}
