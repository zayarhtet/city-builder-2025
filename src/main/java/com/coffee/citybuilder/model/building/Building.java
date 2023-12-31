package com.coffee.citybuilder.model.building;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

/**
 * Class that represents the building logic.
 * 
 * This class saves the buildings position in the map,
 * kepps building parameters such as: if it has electricity,
 * what are its neighbouring buildings and others. It also
 * has helpers methods for processing game logic.
 */
public class Building {
    private boolean hasElectricity = false;
    private List<Building> connections;
    private List<Position> location;
    protected CellItem ct;
    private int quota = 50;

    public Building(List<Position> locations) {
        this.location = new ArrayList<>(locations);
    }

    /**
     * Gets the top left position, used in processing
     * electricity and drawing.
     * 
     * We use top left because a building can take up
     * more than one cell, so as not to process all 4
     * cells we use the top left.
     * 
     * @return top left Position class value
     */
    public Position topLeft() {
        return location.get(0);
    }

    /**
     * Used to check if some position is contained in the list
     * of positions that building occupies.
     * 
     * Used when destroing buildings to see which positions
     * should be deleted.
     * 
     * @return boolean
     */
    public boolean contains(Position d) {
        return location.contains(d);
    }

    /**
     * Returns all the positions that the bulding occupies.
     * 
     * @return arrayList of Position variables
     */
    public List<Position> getLocation() {
        return new ArrayList<>(location);
    }

    /**
     * Returns the price required to build the building.
     * 
     * @return int
     */
    public int getPrice() {
        return ct.price;
    }

    /**
     * Return the CellItem Enum of the building.
     * 
     * Used when checking which type of building this building is.
     * 
     * @return CellItem
     */
    public CellItem getCt() {
        return ct;
    }

    /**
     * Used to check if this building has electricity
     * supplied to it.
     * 
     * @return boolean
     */
    public boolean isHasElectricity() {
        return hasElectricity;
    }

    /**
     * Modifies the hasElectricity value
     * 
     * Used when the power supply is cutoff to false (dismantled power plant,
     * transmission line).
     * Set to true when the electricity is supplied (Power plant nearby,
     * transmission lines connect to power plant).
     * 
     * @param value bool
     */
    public void setHasElectricity(boolean value) {
        this.hasElectricity = value;
        /**
         * Is used to share electricity between neighboring buildings.
         * 
         * The value of how much a building needs electricity is subtracted
         * from the total quota (of how much electricity a building can produce).
         * 
         * @param how much electricity is needed for the other building
         */
    }

    public void Share(int value) {
        this.quota -= value;
    }

    /**
     * Is used to reset supplied electricity to 50
     * 
     * Is used each time when recalculating the electricity
     * supply chains.
     */
    public void resetQuota() {
        this.quota = 50;
    }

    /**
     * Used to check if this building can share it's electricity.
     * 
     * Simple buildings quota is 0, so they cannot, power plant
     * has default of 50.
     * 
     * @param how much electricity would be needed
     * @return boolean, true if it can
     */
    public boolean canShare(int value) {
        // System.out.println("Left "+ quota+" Ask "+value);
        return this.quota >= value;
    }
}
