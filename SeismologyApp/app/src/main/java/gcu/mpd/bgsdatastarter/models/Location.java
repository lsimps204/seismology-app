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

    public Location(String town, String county, Coordinates coordinates) {
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
        String location = this.getTown();
        if (this.getCounty() != null) {
            location += ", " + this.getCounty();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(location.substring(0,1).toUpperCase());

        for (int i=1; i < location.length(); i++) {
            char c = Character.toLowerCase(location.charAt(i));
            if (location.charAt(i-1) == ' ') {
                c = Character.toUpperCase(c);
            }
            sb.append(c);
        }
        return sb.toString().trim();
    }
}