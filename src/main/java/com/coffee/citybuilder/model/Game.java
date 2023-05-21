package com.coffee.citybuilder.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coffee.citybuilder.persistence.Database;

public class Game {
    private final Database database;
    private final List<City> allSavedCities;
    private City currentCity = null;

    public Game() {
        this.database = new Database();
        allSavedCities = database.loadData();
    }

    /**
     * Load a City with id and username.
     * If the id is empty string, then new game is created, and store it in the database
     * @param id ID of the game
     * @param username username of the game
     * @return City Object
     */
    public City loadCity(String id, String username) {
        if (id.length() == 0) {
            City city = new City(username);
            allSavedCities.add(city);
            return city;
        }
        Iterator iter = allSavedCities.iterator();
        while (iter.hasNext()) {
            City city = (City) iter.next();
            if (city.getId().equals(id)) return city;
        }
        return null;
    }

    /**
     * Save the city arraylist into the database.
     */
    public void saveCities() {
        database.saveData(allSavedCities);
    }

    /**
     * Get all cities
     * @return City List
     */
    public List<City> getAllCities() {
        return this.allSavedCities;
    }
}
