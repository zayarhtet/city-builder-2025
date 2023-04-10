package model;

import persistence.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private final Database database;
    private final List<City> allSavedCities;
    private City currentCity = null;

    public Game() {
        this.database = new Database();
        allSavedCities = database.loadData();
    }
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
    public void saveCities() {
//        syncModifiedDate();
        database.saveData(allSavedCities);
    }
    public void removeCity(String id) {
        Iterator iter = allSavedCities.iterator();
        while (iter.hasNext()) {
            City city = (City) iter.next();
            if (city.getId().equals(id)) iter.remove();
        }
    }
    private void syncModifiedDate() {
        Iterator iter = allSavedCities.iterator();
        while (iter.hasNext()) {
            City city = (City) iter.next();
            city.setModifiedDate();
        }
    }
    public List<City> getAllCities() {
        return this.allSavedCities;
    }
}
