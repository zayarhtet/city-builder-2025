package com.coffee.citybuilder.model;

public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean yearGone = false;
    private String currentDateString;

    public DateTime() {
        this(2025, 1, 1, 0, 0);
    }

    public DateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.refreshDateString();
    }

    /**
     * Regenerates the  datetime string which is displayed to the user.in out custom format
     */
    private void refreshDateString() {
        currentDateString = year + "." + (month < 10 ? "0" + month : month) + "." + (day < 10 ? "0" + day : day) + " "
                + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * Returns a strig which represents current date
     * 
     * Used by view to shave in game time
     * 
     * @return String
     */
    public String getDate() {
        return currentDateString;
    }

    /**
     * Move in game tie by a minute
     * 
     * Uses our own logic to move in game time one minute. Handles going over hours/days/etc.
     */
    public void timeMove() {
        this.minute++;
        if (this.minute >= 60) {
            this.hour++;
            this.minute = 0;
            if (this.hour >= 24) {
                this.day++;
                this.hour = 0;
                if (this.day > getMaxDay()) {
                    this.month++;
                    this.day = 1;
                    if (this.month > 12) {
                        year++;
                        this.month = 1;
                        yearGone = true;
                    }
                }
            }
        }
        refreshDateString();
    }

    /**
     * Returns the maximum amount of days in a month
     * 
     * Used when moving time to know when to swith the month
     * @link DateTime#timeMove
     * @return int of max days
     *  
     * */
    public int getMaxDay() {
        switch (this.month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
//                if (isLeapYear(year)) return 29;
                return 28;
        }
        return 0;
    }

    /**
     * Used to know if we have moved up a year
     * 
     * year gone is set to true when time moves changes the yea
     * 
     * @return booean if it's a new year
     */
    public boolean isYearEnd() { return this.yearGone; }

    /**
     *  Check if a new year has happened
     * used?
     */
    public void doneYearEnd() { this.yearGone = false; }
}
