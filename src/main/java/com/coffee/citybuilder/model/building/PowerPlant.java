package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

public class PowerPlant extends Building {
    private int quota = 50;
    public PowerPlant(List<Position> location) {
        super(location);
        super.ct = CellItem.POWER_PLANT;
    }
}
