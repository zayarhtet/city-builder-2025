package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

/**
 * Class to store police department.
 * 
 * Used to identify buildings that are police departments
 * and calculate safety(satiscation) based on building.
 */
public class PoliceDepartment extends Building {
    public static final int radius = 5;

    public PoliceDepartment(List<Position> location) {
        super(location);
        super.ct = CellItem.POLICE_DEPARTMENT;
    }

}
