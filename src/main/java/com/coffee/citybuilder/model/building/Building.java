package com.coffee.citybuilder.model.building;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.model.Position;

public class Building {
    //private final int cost;
    //private final int reimbursement;
    private List<Building> connections;
    private List<Position> location;
    public Building(ArrayList<Position> locations){
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

}
