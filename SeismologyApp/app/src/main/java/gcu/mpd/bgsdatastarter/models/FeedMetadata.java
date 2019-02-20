package gcu.mpd.bgsdatastarter.models;

import java.time.LocalDateTime;

//import androidx.room.Entity;
//import androidx.room.PrimaryKey;

//@Entity(tableName = "metadata")
public class FeedMetadata {

  //  @PrimaryKey
    private int id; //should only ever be one
    private String title;
    private String link;
    private String description;
    private LocalDateTime lastBuildDate;

    public FeedMetadata(String title, String link, String description, LocalDateTime lastBuildDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.lastBuildDate = lastBuildDate;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(LocalDateTime lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }
}
