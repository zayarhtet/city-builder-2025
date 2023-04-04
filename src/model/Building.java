package model;

import java.util.ArrayList;

public class Building {
    //private final int cost;
    //private final int reimbursement;
    ArrayList<Building> connections;
    ArrayList<Position> location;
    public Building(ArrayList<Position> locations){
        this.location = new ArrayList<>(locations);
    }

    public Position topLeft(){
        return location.get(0);
    }

}
