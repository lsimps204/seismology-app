package gcu.mpd.bgsdatastarter.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

//import androidx.room.Embedded;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;

/* Model/POJO to represent individual Earthquake data */

//@Entity(tableName = "earthquakes")
public class Earthquake
{
  //  @PrimaryKey
    private int uid;

    public String title;
    public String link;
    public String category;
    public LocalDateTime pubDate;

    //@Embedded
    public Location location;
    public int depth;
    public String depthUnit;
    public float magnitude;

    /* No argument constructor */
    public Earthquake() {}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getDepthUnit() {
        return depthUnit;
    }

    public void setDepthUnit(String depthUnit) {
        this.depthUnit = depthUnit;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

}