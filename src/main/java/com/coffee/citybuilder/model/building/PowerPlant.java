package com.coffee.citybuilder.model.building;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import java.util.List;

/**
 * Class to implement power plant functionality
 * why?
 */
public class PowerPlant extends Building {
    //private int quota = 50;
    public PowerPlant(List<Position> location) {
        super(location);
        super.ct = CellItem.POWER_PLANT;
    }

//    public void Share(int value){
//        this.quota -= value;
//    }
//    public void resetQuota() { this.quota = 50; }
//    public boolean canShare(int value) { return this.quota >= value; }
}
