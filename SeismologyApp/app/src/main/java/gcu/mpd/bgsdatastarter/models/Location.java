package gcu.mpd.bgsdatastarter.models;

public class Location {

    private String town;
    private String county;
    private Coordinates coordinates;

    public Location(String town, String region, Coordinates coordinates) {
        this.town = town;
        this.county = county;
        this.coordinates = coordinates;
    }


    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    @Override
    public String toString() {
        return this.town + ", " + this.county + " (" + this.coordinates + ")";
    }
}