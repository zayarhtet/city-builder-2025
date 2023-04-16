package tests;

import junit.framework.TestCase;
import model.City;

public class TestBasic extends TestCase {
  public void testTrue() {
    System.out.println("Test1");
    assertTrue(true);
  }
  public void testCityColumns() {
    City tempCity = new City("test");
    assertEquals(tempCity.getColumnCount(), 33);
  }
}
