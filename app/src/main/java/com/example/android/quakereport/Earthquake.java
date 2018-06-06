package com.example.android.quakereport;

public class Earthquake {
    private double magnatuide;
    private String location;
    private long date;
    private String earthquakeDetailUrl;

    /** Title of the earthquake event */
    private  String title;

    /** Number of people who felt the earthquake and reported how strong it was */
    private String numOfPeople;

    /** Perceived strength of the earthquake from the people's responses */
    private  String perceivedStrength;

    public Earthquake(double magnatuide,String location,long date,String earthquakeDetailUrl){
        this.magnatuide=magnatuide;
        this.location=location;
        this.date=date;
        this.earthquakeDetailUrl=earthquakeDetailUrl;
    }

    /**
     * Constructs a new {@link Earthquake}.
     *
     * @param eventTitle is the title of the earthquake event
     * @param eventNumOfPeople is the number of people who felt the earthquake and reported how
     *                         strong it was
     * @param eventPerceivedStrength is the perceived strength of the earthquake from the responses
     */
    public Earthquake(String eventTitle, String eventNumOfPeople, String eventPerceivedStrength) {
        title = eventTitle;
        numOfPeople = eventNumOfPeople;
        perceivedStrength = eventPerceivedStrength;
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

    public String getTitle() {
        return title;
    }

    public String getNumOfPeople() {
        return numOfPeople;
    }

    public String getPerceivedStrength() {
        return perceivedStrength;
    }
}
