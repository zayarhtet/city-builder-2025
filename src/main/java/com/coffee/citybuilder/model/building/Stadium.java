package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

/**
 * Class to implement stadium functionality.
 * 
 * Used to identify buildings that are Stadiums
 * and calculate the satisfaction, because stadiums
 * increase satisfaction by a certain amount.
 */
public class Stadium extends Building {
    private int population = 60000;

    public Stadium(List<Position> location) {
        super(location);
        super.ct = CellItem.STADIUM;
    }
}
