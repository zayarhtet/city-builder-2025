package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

public class Zone {
    private Position location;
    protected int numOfPeople;
    protected CellItem ct;
    private boolean canWork = false;
    private int workerCapacity = 40;
    private boolean hasElectricity = false;
    public Zone(Position p) {
        location = new Position(p);
    }

    public CellItem getCt() { return ct; }
    public boolean isAt(Position p) { return this.location.equals(p); }
    public int getPopulation() { return this.numOfPeople; }
    public Position getLocation() { return this.location; }
    public void setCanWork(boolean value){ this.canWork = value; }
    public boolean getCanWork(){ return this.canWork; }
    public boolean isHasElectricity(){ return hasElectricity; }
    public void setHasElectricity(boolean value){ this.hasElectricity = value; }
    public void increaseWorkers(int value){
//        if (numOfPeople == workerCapacity) return false;
        numOfPeople = Math.min(numOfPeople+value,workerCapacity);
//        return true;
    }
    public void decreaseWorkers() { numOfPeople = 0; }
    public int getWorkerCapacity() { return workerCapacity; }
}
