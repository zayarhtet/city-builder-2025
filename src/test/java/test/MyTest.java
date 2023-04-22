package test;


import org.junit.Test;
import static org.junit.Assert.assertTrue;


public class MyTest  {
  @Test
  public void testTrue() {
    System.out.println("Test1");
    assertTrue(true);
  }
  // @Test
  // public void testCityColumns() {
  //   City tempCity = new City("test");
  //   assertEquals(tempCity.getColumnCount(), 33);
  // }
}
