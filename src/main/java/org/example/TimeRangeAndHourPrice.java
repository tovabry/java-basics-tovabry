package org.example;

public class TimeRangeAndHourPrice {

    private int timpris;
    private String timeRange;

    public TimeRangeAndHourPrice(int timpris, String timeRange) {
        this.timpris = timpris;
        this.timeRange = timeRange;
    }

    public int getTimpris() {
        return timpris;
    }

    public String getTimeRange() {
        return timeRange;
    }

    @Override
    public String toString() {
        return timeRange + " " +  timpris;
    }
}


