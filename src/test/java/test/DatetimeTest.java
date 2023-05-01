package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.coffee.citybuilder.model.DateTime;

public class DatetimeTest {

    private int year = 2023;
    private int month = 12;
    private int day = 31;
    private int hour = 23;
    private int minute = 59;
    private int second = 59;

    private String currentDateString;
    // Some date
    // Test when we move to 60 seconds
    // Test when we move to 60 minutes
    // Test when 24 hours
    // Test if string are equal
    // Test if moth has correct amount of days
    // Test when new month
    // Test when new year

    private DateTime getDatetime(int year, int month, int day, int hour, int minute, int second) {
        return new DateTime(year, month, day, hour, minute, second);
    }

    @Test
    public void test_OverloadSecond() {
        DateTime datetime = getDatetime(year, month, day, hour, 58, 58);
        datetime.timeMove();
        assertEquals("2023.12.31 23:59", datetime.getDate());

    }

    @Test
    public void test_OverloadMinute() {
        DateTime datetime = getDatetime(year, month, day, hour, 58, second);
        datetime.timeMove();
        assertEquals("2023.12.31 23:59", datetime.getDate());

    }

    @Test
    public void test_OverloadHour() {
        DateTime datetime = getDatetime(year, month, day, 22, minute, second);
        datetime.timeMove();
        //
        assertEquals("2023.12.31 23:00", datetime.getDate());

    }

    @Test
    public void test_OverloadDay() {
        DateTime datetime = getDatetime(year, month, 30, hour, minute, second);
        datetime.timeMove();
        assertEquals("2023.12.31 00:00", datetime.getDate());

    }

    @Test
    public void test_OverloadMonth() {
        DateTime datetime = getDatetime(year, month, day, hour, minute, second);
        datetime.timeMove();
        assertEquals("2024.01.01 00:00", datetime.getDate());

    }

    @Test
    public void test_OverloadYear() {
        DateTime datetime = getDatetime(year, month, day, hour, minute, second);
        datetime.timeMove();
        assertEquals("2024.01.01 00:00", datetime.getDate());

    }
}
