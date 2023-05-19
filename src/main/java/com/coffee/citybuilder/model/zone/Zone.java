package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Citizen;
import com.coffee.citybuilder.model.Position;

import java.util.Iterator;
import java.util.List;

/**
 * Class to store zone information on a map?
 * 
 * 
 */
public class Zone {
    private Position location;
    protected int numOfPeople;
    protected List<Citizen> citizens;
    protected int pensionerCount;
    protected CellItem ct;
    private boolean canWork = false;
    private int workerCapacity = 40;
    private boolean hasElectricity = false;
    private boolean hasPolice = false;
    private boolean hasStadium = false;
    /
    public Zone(Position p) {
        location = new Position(p);
    }

    /**
     * Used to chech which zone this is
     * 
     * @return CelItem emum of that zone
     */
    public CellItem getCt() { return ct; }
    /**
     * Used to chekc if somenthing should be apllied to this zone?
     * 
     * @return boolean if this zone is at position p
     */
    public boolean isAt(Position p) { return this.location.equals(p); }
    /**
     * Used when...
     * 
     * @return integer nubmer of people currently lnving in the zone?
     */
    public int getPopulation() { return this.numOfPeople; }
    public Position getLocation() { return this.location; }
    public void setCanWork(boolean value){ this.canWork = value; }
    public boolean getCanWork(){ return this.canWork; }
    public boolean isHasElectricity(){ return hasElectricity; }
    public boolean isHasPolice(){ return hasPolice; }
    public boolean isHasStadium(){ return hasStadium; }

    public void setHasElectricity(boolean value){ this.hasElectricity = value; }
    public void setHasPolice(boolean value){ this.hasPolice = value; }
    public void setHasStadium(boolean value){ this.hasStadium = value; }
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
