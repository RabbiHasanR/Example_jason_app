package com.example.android.quakereport;

public class Earthquake {
    private double magnatuide;
    private String location;
    private long date;
    private String earthquakeDetailUrl;

    public Earthquake(double magnatuide,String location,long date,String earthquakeDetailUrl){
        this.magnatuide=magnatuide;
        this.location=location;
        this.date=date;
        this.earthquakeDetailUrl=earthquakeDetailUrl;
    }

    public double getMagnatuide() {
        return magnatuide;
    }

    public void setMagnatuide(double magnatuide) {
        this.magnatuide = magnatuide;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getEarthquakeDetailUrl() {
        return earthquakeDetailUrl;
    }

    public void setEarthquakeDetailUrl(String earthquakeDetailUrl) {
        this.earthquakeDetailUrl = earthquakeDetailUrl;
    }
}
