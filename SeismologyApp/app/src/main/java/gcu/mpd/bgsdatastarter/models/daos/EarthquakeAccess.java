package gcu.mpd.bgsdatastarter.models.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;

@Dao
public interface EarthquakeAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertQuakes(List<Earthquake> earthquakes);

    @Query("SELECT * FROM earthquakes")
    List<Earthquake> getAllEarthquakes();


}
