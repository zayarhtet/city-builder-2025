package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.model.Position;


// TODO:
// build Road (done in is Road)
// assign Zone when not occupied
// assign zone when occupied
// Construct building when occupied 
// Construct building when not occupied
// Demolish Road
// Demolish Building 1x1
// Demolish building 2x2
// Delete zone
// Delete transmission line
// delete building
// getCellItem
// getColumnCount
// getRowCount
// isOccupied
// isRoad when not road/when road
// isVRoad when not road/when road
public class CityTest {
    private  City city = new City("test");
       
    @Test
    public void testCellItemIfGeneral() {
        assertEquals(city.getCellItem(0, 0), CellItem.GENERAL);
    }

    @Test
    public void testGetColumnCount() {
        assertEquals(city.getColumnCount(), 33);
    }
    @Test
    public void testGetRowCount() {
        assertEquals(city.getRowCount(), 22);
    }
    @Test
    public void testIsOccupiedWhenNotOccupied() {
        assertTrue(!city.isOccupied(new Position(0, 0)));
    }
    @Test
    public void testIsOccupiedWhenOccupied() {
        city.constructBuilding(new Position(0, 0), CellItem.H_ROAD);
        assertTrue(city.isOccupied(new Position(0, 0)));
        city.constructBuilding(new Position(0, 0), CellItem.GENERAL);
    }
    // TODO:
    @Test
    public void testDemolishWhenOccupied() {
        // city.constructBuilding(new Position(0, 0), CellItem.H_ROAD);
        // assertEquals(city.getRowCount(), 22);
        // city.demolish(new Position(0, 0), CellItem.H_ROAD);
    }
    @Test
    public void testIsRoadWhenNoRoad(){
        assertTrue(!city.isRoad(0, 0));
    }
    @Test
    public void testIsRoadWhenRoad(){
        city.buildRoad(new Position(0, 0), CellItem.H_ROAD);
        assertTrue(city.isRoad(0, 0));
    }
    
}
