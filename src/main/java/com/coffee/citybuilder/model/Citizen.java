package com.coffee.citybuilder.model;

public class Citizen {
    private int currentAge;
    private int dyingAge;
    private boolean alive = true;
    public Citizen (int dyingAge) {
        this.currentAge = 18;
        this.dyingAge = dyingAge;
    }
    public void increaseAge() { currentAge++; if (currentAge == dyingAge) alive = false; }
    public boolean isAlive() { return alive; }
    public boolean isPensioner() { return currentAge >= 65; }
}
