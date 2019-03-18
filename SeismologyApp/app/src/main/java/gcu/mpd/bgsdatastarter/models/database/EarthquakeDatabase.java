package gcu.mpd.bgsdatastarter.models.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.TypeConverters.DateConverter;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeAccess;

@Database(entities={Earthquake.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class EarthquakeDatabase extends RoomDatabase {

    public abstract EarthquakeAccess earthquakeAccess();

}
