package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Citizen;
import com.coffee.citybuilder.model.Position;

import java.util.Iterator;
import java.util.List;

public class Zone {
    private Position location;
    protected int numOfPeople;
    protected List<Citizen> citizens;
    protected int pensionerCount;
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
        numOfPeople = Math.min(numOfPeople+value,workerCapacity);
    }
    public void increaseAge() {
        Iterator it = citizens.iterator();
        pensionerCount = 0;
        while (it.hasNext()) {
            Citizen c = (Citizen) it.next();
            c.increaseAge();
            if (!c.isAlive()) {
                it.remove();
                numOfPeople--;
            }
            if (c.isPensioner()) pensionerCount++;
        }
    }
    public int getPensionerCount() { return pensionerCount; }
    public void decreaseWorkers() { numOfPeople = 0; }
    public int getWorkerCapacity() { return workerCapacity; }
}
