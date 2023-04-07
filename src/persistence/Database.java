package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import model.City;

public class Database {
    private Gson gson;
    private String filePath;

    public Database(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = "src/resource/database/userdata.json";
    };

    public void saveData(List<City> cities){
        try {
            FileWriter writer = new FileWriter(this.filePath);
            gson.toJson(cities, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {System.out.println(e.toString());}
    }

    public List<City> loadData(){
        Type citiesListType = new TypeToken<ArrayList<City>>(){}.getType();

        try (Reader reader = new FileReader(this.filePath)) {
            List<City> cities = gson.fromJson(reader, citiesListType);
//            System.out.println(cities);
            return cities;
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

}
