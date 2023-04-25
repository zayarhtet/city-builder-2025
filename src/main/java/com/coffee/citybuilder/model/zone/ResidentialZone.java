package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import static com.coffee.citybuilder.resource.Constant.Initial_Population;

public class ResidentialZone extends Zone {
    public ResidentialZone(Position p) {
        super(p); super.ct = CellItem.RESIDENTIAL;
        super.numOfPeople = Initial_Population;
    }
}
