package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

public class PoliceDepartment extends Building {
    public static final int radius = 5;
    public PoliceDepartment(List<Position> location) {
        super(location);
        super.ct = CellItem.POLICE_DEPARTMENT;
    }

}
