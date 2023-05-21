package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Citizen;
import com.coffee.citybuilder.model.Position;

import java.util.Iterator;
import java.util.List;

/**
 * Class to store zone information on a code
 * map representation for the logic and CityMap
 * visual representation for the GUI.
 * 
 * It has the base parameters that all zones have
 * and is extended for more specific zones.
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

    public Zone(Position p) {
        location = new Position(p);
    }

    /**
     * Used to chech which zone it is. (Industrial/Service/Residential)
     * 
     * @return CelItem emum of that zone
     */
    public CellItem getCt() {
        return ct;
    }

    /**
     * Used when deleting zones. The method in city iterates zones and
     * base don the given position tries to find the zone to be deleted.
     * 
     * @return boolean if this zone is at position p
     */
    public boolean isAt(Position p) {
        return this.location.equals(p);
    }

    /**
     * Used for population calculations.
     * 
     * When calculating total population, satisfaction, displaying
     * single zone information and so on.
     * 
     * @return integer nubmer of people currently lnving in the zone
     */
    public int getPopulation() {
        return this.numOfPeople;
    }

    public Position getLocation() {
        return this.location;
    }

    public void setCanWork(boolean value) {
        this.canWork = value;
    }

    public boolean getCanWork() {
        return this.canWork;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public boolean isHasPolice() {
        return hasPolice;
    }

    public boolean isHasStadium() {
        return hasStadium;
    }

    public void setHasElectricity(boolean value) {
        this.hasElectricity = value;
    }

    public void setHasPolice(boolean value) {
        this.hasPolice = value;
    }

    public void setHasStadium(boolean value) {
        this.hasStadium = value;
    }

    public void increaseWorkers(int value) {
        numOfPeople = Math.min(numOfPeople + value, workerCapacity);
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
            if (c.isPensioner())
                pensionerCount++;
        }
    }

    public int getPensionerCount() {
        return pensionerCount;
    }

    public void decreaseWorkers() {
        numOfPeople = 0;
    }

    public int getWorkerCapacity() {
        return workerCapacity;
    }
}
