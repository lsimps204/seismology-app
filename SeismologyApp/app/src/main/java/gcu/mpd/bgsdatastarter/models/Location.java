package gcu.mpd.bgsdatastarter.models;

//import androidx.room.Embedded;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "location")
public class Location {
    private String town;
    private String county;

    @Embedded
    private Coordinates coordinates;

    public Location() {}

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