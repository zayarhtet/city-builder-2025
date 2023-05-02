package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.coffee.citybuilder.model.Position;

public class PositionTest {

    @Test
    public void test_PositionEquals_WhenEqual() {
        Position positionOne = new Position(0, 0);
        Position positionTwo = new Position(0, 0);
        assertTrue(positionOne.equals(positionTwo));
    }

    @Test
    public void test_PositionEquals_WhenNotEqual_x() {
        Position positionOne = new Position(0, 0);
        Position positionTwo = new Position(1, 0);
        assertTrue(!positionOne.equals(positionTwo));
    }

    @Test
    public void test_PositionEquals_WhenNotEqual_y() {
        Position positionOne = new Position(0, 0);
        Position positionTwo = new Position(0, 1);
        assertTrue(!positionOne.equals(positionTwo));
    }

    @Test
    public void test_PositionEquals_WhenNotEqual_xy() {
        Position positionOne = new Position(0, 0);
        Position positionTwo = new Position(1, 1);
        assertTrue(!positionOne.equals(positionTwo));
    }

}
