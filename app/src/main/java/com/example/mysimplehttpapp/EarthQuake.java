package com.example.mysimplehttpapp;

public class EarthQuake {
    String distance_loc;
    long date;
    double magnitude;
    String earthquakeSpecificURL;

    public String getEarthquakeSpecificURL() {
        return earthquakeSpecificURL;
    }

    public void setEarthquakeSpecificURL(String earthquakeSpecificURL) {
        this.earthquakeSpecificURL = earthquakeSpecificURL;
    }

    public EarthQuake(String distance_loc, long date, double magnitude, String url) {
        this.distance_loc = distance_loc;
        this.date = date;
        this.magnitude = magnitude;
        this.earthquakeSpecificURL = url;
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
