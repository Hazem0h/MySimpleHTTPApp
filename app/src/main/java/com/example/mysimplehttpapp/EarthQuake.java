package com.example.mysimplehttpapp;

public class EarthQuake {
    String distance_loc;
    long date;
    double magnitude;

    public EarthQuake(String distance_loc, long date, double magnitude) {
        this.distance_loc = distance_loc;
        this.date = date;
        this.magnitude = magnitude;
    }

    public String getDistance_loc() {
        return distance_loc;
    }

    public void setDistance_loc(String distance_loc) {
        this.distance_loc = distance_loc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
}
