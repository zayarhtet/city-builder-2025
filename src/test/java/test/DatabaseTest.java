package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.persistence.Database;
// DONE
public class DatabaseTest {
    private Database db = new Database();
    @Test
    public void testIfFileCreated() {
        db.loadData();
        String PATH = "./src/main/java/com/coffee/citybuilder/resource/database/userdata.json";
        File f = new File(PATH);
        assert f.exists() && !f.isDirectory();
    }
    @Test
    public void testIfCityCanBeSaved(){
        // reading cities
        String name = "TempCityForTestClass";
        List<City> citiesTemp = db.loadData();
        List<City> citiesNotModified = new ArrayList<>(citiesTemp);
        //add temporary city
        City tempCity = new City(name);
        citiesTemp.add(tempCity);
        db.saveData(citiesTemp);
        // load cities and check if temp is There
        citiesTemp = db.loadData();
        Iterator iter = citiesTemp.iterator();
        boolean found = false;
        while (iter.hasNext()) {
            City city = (City) iter.next();
            if (city.getUsername().equals(name)) {
                found = true;
            };
        }
        // cleanup
        db.saveData(citiesNotModified);
        assertTrue(found);
    }

}
