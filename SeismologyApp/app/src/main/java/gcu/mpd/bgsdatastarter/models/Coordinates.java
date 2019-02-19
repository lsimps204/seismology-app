package gcu.mpd.bgsdatastarter.models;

public class Coordinates {

    private float lat;
    private float lon;

    public Coordinates() {}

    public Coordinates(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return this.lat + ", " + this.lon;
    }
}