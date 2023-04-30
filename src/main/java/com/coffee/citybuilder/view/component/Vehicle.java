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

    public boolean isGoRightOrDown() {
        return goRightOrDown;
    }

    public void setGoRightOrDown() {
        if (this.goRightOrDown) this.goRightOrDown = false;
        else this.goRightOrDown = true;
    }
}
