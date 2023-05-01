package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.City;
import com.coffee.citybuilder.model.Position;


// TODO:
// build Road +
// assign Zone (occupied, !occupied) +
// Construct building (occupied , !occupied) +
// Demolish Road +
// Demolish building 2x2 +
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
    int x = 0;
    int y = 0;
    private Position temporaryPosition = new Position(x, y);

       
    private void cleanCell(int x, int y){
        
        city.deleteRoad(temporaryPosition);
        city.assignZone(temporaryPosition, CellItem.GENERAL);
        city.deleteBuilding(temporaryPosition);
    }

    @Test
    public void test_BuildRoad_WhenNoRoad(){
        city.buildRoad(temporaryPosition, CellItem.H_ROAD);
        assert city.isRoad(0, 0);
        cleanCell(0, 0);
    }

    @Test
    public void test_AssignZone_WhenNotOccupied(){
        city.assignZone(temporaryPosition, CellItem.RESIDENTIAL);
        assertEquals(city.getCellItem(x, y), CellItem.RESIDENTIAL); ;
        cleanCell(x, y);
    }
    @Test
    public void test_AssignZone_WhenOccupied(){
        city.assignZone(temporaryPosition, CellItem.RESIDENTIAL);
        city.assignZone(temporaryPosition, CellItem.SERVICE_INDUSTRIAL);

        assertEquals(city.getCellItem(x, y), CellItem.RESIDENTIAL); ;
        cleanCell(x, y);
    }

    @Test
    public void test_ConstructBuilding_WhenNotOccupied(){
        city.constructBuilding(temporaryPosition, CellItem.POLICE_DEPARTMENT);
        assertEquals(city.getCellItem(x, y), CellItem.POLICE_DEPARTMENT); ;
        cleanCell(x, y);
    }
    @Test
    public void test_ConstructBuilding_WhenOccupied(){
        city.constructBuilding(temporaryPosition, CellItem.POLICE_DEPARTMENT);
        city.constructBuilding(temporaryPosition, CellItem.POLICE_DEPARTMENT);

        assertEquals(city.getCellItem(x, y), CellItem.POLICE_DEPARTMENT);
        assertEquals(city.getCellItem(x, y+1), CellItem.POLICE_DEPARTMENT);

        cleanCell(x, y);
    }

    @Test
    public void test_DemolishRoad(){
        city.buildRoad(temporaryPosition, CellItem.H_ROAD);
        city.deleteRoad(temporaryPosition);
        assertEquals(city.getCellItem(x, y), CellItem.GENERAL);
    }

    @Test
    public void test_DemolishBuilding(){
        city.constructBuilding(temporaryPosition, CellItem.STADIUM);
        city.demolish(temporaryPosition);
        assertEquals(city.getCellItem(x, y), CellItem.GENERAL);
        assertEquals(city.getCellItem(x, y+1), CellItem.GENERAL);
    }

    @Test
    public void test_DemolishZone(){
        city.assignZone(temporaryPosition, CellItem.SERVICE_INDUSTRIAL);
        city.deleteZone(temporaryPosition);
        assertEquals(city.getCellItem(x, y), CellItem.GENERAL);
    }




    // @Test
    // public void testCellItemIfGeneral() {
    //     assertEquals(city.getCellItem(0, 0), CellItem.GENERAL);
    // }

    // @Test
    // public void testGetColumnCount() {
    //     assertEquals(city.getColumnCount(), 33);
    // }
    // @Test
    // public void testGetRowCount() {
    //     assertEquals(city.getRowCount(), 22);
    // }
    // @Test
    // public void testIsOccupiedWhenNotOccupied() {
    //     assertTrue(!city.isOccupied(new Position(0, 0)));
    // }
    // @Test
    // public void testIsOccupiedWhenOccupied() {
    //     city.constructBuilding(new Position(0, 0), CellItem.H_ROAD);
    //     assertTrue(city.isOccupied(new Position(0, 0)));
    //     city.constructBuilding(new Position(0, 0), CellItem.GENERAL);
    // }
    // // TODO:
    // @Test
    // public void testDemolishWhenOccupied() {
    //     // city.constructBuilding(new Position(0, 0), CellItem.H_ROAD);
    //     // assertEquals(city.getRowCount(), 22);
    //     // city.demolish(new Position(0, 0), CellItem.H_ROAD);
    // }
    // @Test
    // public void testIsRoadWhenNoRoad(){
    //     assertTrue(!city.isRoad(0, 0));
    // }
    // @Test
    // public void testIsRoadWhenRoad(){
    //     city.buildRoad(new Position(0, 0), CellItem.H_ROAD);
    //     assertTrue(city.isRoad(0, 0));
    // }
    
}
