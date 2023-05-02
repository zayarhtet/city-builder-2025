package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Citizen;
import com.coffee.citybuilder.model.Position;

import java.util.ArrayList;
import java.util.Random;

import static com.coffee.citybuilder.resource.Constant.Initial_Population;

public class ResidentialZone extends Zone {

    public ResidentialZone(Position p) {
        super(p); super.ct = CellItem.RESIDENTIAL;
        super.citizens = new ArrayList<>(); super.numOfPeople = 0;
        Random rand = new Random();
        for (int i = 0; i < Initial_Population; i++) {
            citizens.add(new Citizen(rand.nextInt(82)));
            numOfPeople++;
        }
    }


}
