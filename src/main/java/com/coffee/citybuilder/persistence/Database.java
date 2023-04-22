package com.coffee.citybuilder.persistence;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.coffee.citybuilder.model.City;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Gson gson;
    private String filePath;
    private String fileName;

    public Database(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = "./src/main/java/com/coffee/citybuilder/resource/database";
        this.fileName = "userdata.json";
    };

    public void saveData(List<City> cities){
        try {
            FileWriter writer = new FileWriter(this.filePath + "/" + this.fileName);
            gson.toJson(cities, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {System.out.println(e.toString());}
    }

    public List<City> loadData() {
        Type citiesListType = new TypeToken<ArrayList<City>>(){}.getType();

        try (Reader reader = new FileReader(this.filePath + "/" + this.fileName)) {
            List<City> cities = gson.fromJson(reader, citiesListType);
            return cities;
        } catch (IOException e) {
            try {
                File directory = new File(this.filePath);
                if (! directory.exists()) { directory.mkdirs(); }

                PrintWriter writer = new PrintWriter(this.filePath + "/" + this.fileName, "UTF-8");
                writer.println("[]");
                writer.close();
            } catch (IOException ex) { ex.printStackTrace();System.out.println("Working Directory = " + System.getProperty("user.dir")); }
            return loadData();
        }
    }

}
