package com.coffee.citybuilder.persistence;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.coffee.citybuilder.model.City;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Database class that is used to save the current state of the games.
 * 
 * This class works with json files. Into a json files it writes the
 * current game after it's finished or can load all games from last session.
 */
public class Database {
    private Gson gson;
    private String filePath;
    private String fileName;

    public Database() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = "./src/main/java/com/coffee/citybuilder/resource/database";
        this.fileName = "userdata.json";
    };

    /**
     * Saves the current game into a json file.
     * 
     * We pass all games so that the serialization with Gson would be easier.
     * 
     * @param cities - list of City objects that represent the current and all the
     *               previous games.
     */
    public void saveData(List<City> cities) {
        try {
            FileWriter writer = new FileWriter(this.filePath + "/" + this.fileName);
            gson.toJson(cities, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Loads all prevuos games from json file
     * 
     * Based on the save path it, it uses Gson to transforn the
     * json data into a list of City objects. It also checks if
     * the file is saved in that path, if not it creates such file
     * and reutrns an empty list.
     * 
     * @return list of City objects, from which we can restore the game
     */
    public List<City> loadData() {
        Type citiesListType = new TypeToken<ArrayList<City>>() {
        }.getType();

        try (Reader reader = new FileReader(this.filePath + "/" + this.fileName)) {
            List<City> cities = gson.fromJson(reader, citiesListType);
            return cities;
        } catch (IOException e) {
            try {
                File directory = new File(this.filePath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                PrintWriter writer = new PrintWriter(this.filePath + "/" + this.fileName, "UTF-8");
                writer.println("[]");
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Working Directory = " + System.getProperty("user.dir"));
            }
            return loadData();
        }
    }

}
