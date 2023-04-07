package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import model.City;

public class Database {
    private Gson gson;
    private String filePath;

    public Database(){
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        // this.gson = new Gson();
        this.filePath = "./SaveGames/" + filePath + ".json";
    };

    public void saveGame(City city){
        System.out.println(filePath);
        try {
            FileWriter writer = new FileWriter(this.filePath);
            gson.toJson(city, writer);
            writer.flush();
            writer.close();
            System.out.println("File Written");
        } catch (IOException e) {System.out.println(e.toString());}
    }

    public City loadGame(){

        try (Reader reader = new FileReader(this.filePath)) {

            // Convert JSON File to Java Object
            City city = gson.fromJson(reader, City.class);

            // print staff object
            System.out.println(city);
            return city;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
