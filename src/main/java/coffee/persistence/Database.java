package coffee.persistence;

// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import coffee.model.City;

public class Database {
    private Gson gson;
    private String filePath;
    private String fileName;

    public Database(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // this.gson = new GsonBuilder.setPrettyPrinting().create();
        this.filePath = "./src/main/java/coffee/resource/database";
        this.fileName = "userdata.json";
        // this.filePath = getClass().getResource("userdata.json");

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
                if (! directory.exists()){
                    directory.mkdirs();
                    // If you require it to make the entire directory path including parents,
                    // use directory.mkdirs(); here instead.
                }

                File file = new File(this.filePath + "/" + this.fileName);
                try{
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("[]");
                    bw.close();
                } catch (IOException ex){
                    e.printStackTrace();
                    System.exit(-1);
                }
            
                PrintWriter writer = new PrintWriter(this.filePath + "/" + this.fileName, "UTF-8");
                writer.println("[]");
                writer.close();
            } catch (IOException ex) { ex.printStackTrace();System.out.println("Working Directory = " + System.getProperty("user.dir")); }
            return loadData();
        }
    }

}
