package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

public class Stadium extends Building {
    private int population = 60000;
    public Stadium(List<Position> location) {
        super(location);
        super.ct = CellItem.STADIUM;
    }
}
