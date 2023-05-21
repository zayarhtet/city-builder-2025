package com.coffee.citybuilder.model.zone;

/**
 * Class that returns the parameters needed for the user to see.
 * 
 * Is used when a user clicks on a zone, this class is created and returned
 * because it has all the information relevant to the zone, which Zone class
 * might not.
 * (for example satisfaction)
 * 
 */
public class ZoneInfo {
    public int population, pensionerCount, employedCount;
    public double satisfaction;
    public boolean police, stadium, electricity;

    public ZoneInfo(int population, int pensionerCount, double satisfaction, int employedCount, boolean police,
            boolean stadium, boolean electricity) {
        this.population = population;
        this.pensionerCount = pensionerCount;
        this.satisfaction = satisfaction;
        this.employedCount = employedCount;
        this.police = police;
        this.stadium = stadium;
        this.electricity = electricity;
    }
}
