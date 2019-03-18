package gcu.mpd.bgsdatastarter.models.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;

@Dao
public interface EarthquakeDao {

    // Retrieves all the earthquakes as a list wrapped in LiveData
    // LiveData means the result is observable
    @Query("SELECT * FROM earthquakes")
    public LiveData<List<Earthquake>> getAllEarthquakes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Earthquake... earthquakes);

    // Helper that deletes all entities from the table
    @Query("DELETE FROM earthquakes")
    void deleteAllEarthquakes();
}
