package com.coffee.citybuilder.view.component;

import com.coffee.citybuilder.view.CityMap;

public class Vehicle {
    private int value;
    private boolean goRightOrDown = true;

    public Vehicle (int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Check the direction
     * @return boolean
     */
    public boolean isGoRightOrDown() {
        return goRightOrDown;
    }

    /**
     * Set the direction
     */
    public void setGoRightOrDown() {
        if (this.goRightOrDown) this.goRightOrDown = false;
        else this.goRightOrDown = true;
    }
}
