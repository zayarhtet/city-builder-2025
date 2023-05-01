package com.coffee.citybuilder.model.building;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

public class Building {
    //private final int cost;
    //private final int reimbursement;
    private boolean hasElectricity = false;
    private List<Building> connections;
    private List<Position> location;
    protected CellItem ct;
    private int quota = 50;
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
    public boolean isHasElectricity(){ return hasElectricity; }
    public void setHasElectricity(boolean value){ this.hasElectricity = value; }
    public void Share(int value){ this.quota -= value; }
    public void resetQuota() { this.quota = 50; }
    public boolean canShare(int value) {
        //System.out.println("Left "+ quota+" Ask "+value);
        return this.quota >= value;
    }
}
