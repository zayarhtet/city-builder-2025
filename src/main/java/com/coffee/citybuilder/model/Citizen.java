package com.coffee.citybuilder.model;

public class Citizen {
    private int currentAge;
    private int dyingAge;
    private boolean alive = true;
    public Citizen (int dyingAge) {
        this.currentAge = 18;
        this.dyingAge = dyingAge;
    }

    /**
     * Increase the age of citizen
     */
    public void increaseAge() { currentAge++; if (currentAge == dyingAge) alive = false; }

    /**
     * Check if citizen is alive
     * @return Boolean
     */
    public boolean isAlive() { return alive; }

    /**
     * Check if the citizen is pensioner
     * @return
     */
    public boolean isPensioner() { return currentAge >= 65; }
}
