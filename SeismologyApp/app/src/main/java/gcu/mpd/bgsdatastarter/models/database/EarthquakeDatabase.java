package gcu.mpd.bgsdatastarter.models.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeAccess;

@Database(entities={Earthquake.class}, version = 1, exportSchema = false)
public abstract class EarthquakeDatabase extends RoomDatabase {

    public abstract EarthquakeAccess earthquakeAccess();

}
