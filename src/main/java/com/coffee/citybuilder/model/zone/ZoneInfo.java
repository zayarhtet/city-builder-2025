package com.coffee.citybuilder.model.zone;

public class ZoneInfo {
    public int population, pensionerCount, satisfaction, employedCount;
    public boolean police,stadium, electricity;

    public ZoneInfo(int population, int pensionerCount, int satisfaction, int employedCount, boolean police, boolean stadium, boolean electricity) {
        this.population = population;
        this.pensionerCount = pensionerCount;
        this.satisfaction = satisfaction;
        this.employedCount = employedCount;
        this.police = police;
        this.stadium = stadium;
        this.electricity = electricity;
    }
}
