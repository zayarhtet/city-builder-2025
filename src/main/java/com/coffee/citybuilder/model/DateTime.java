package com.coffee.citybuilder.model;

public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    private String currentDateString;

    public DateTime() {
        this(2025, 1, 1, 0, 0, 0);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.refreshDateString();
        System.out.println("Called");
    }

    private void refreshDateString() {
        currentDateString = year + "." + (month < 10 ? "0" + month : month) + "." + (day < 10 ? "0" + day : day) + " "
                + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    public String getDate() {
        System.out.println(currentDateString + ":" + this.second);
        return currentDateString;
    }

    public void timeMove() {
        this.second++;
        if (this.second >= 60) {
            this.minute++;
            this.second = 0;
            if (this.minute >= 60) {
                this.hour++;
                this.minute = 0;
                if (this.hour >= 24) {
                    this.day++;
                    this.hour = 1;
                    if (this.day > getMaxDay()) {
                        this.month++;
                        this.day = 1;
                        if (this.month > 12) {
                            year++;
                            this.month = 1;
                        }
                    }
                }
            }
            refreshDateString();
        }
    }

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
                return 28;
        }
        return 0;
    }

}
