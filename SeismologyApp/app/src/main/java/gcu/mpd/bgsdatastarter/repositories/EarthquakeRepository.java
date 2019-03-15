package gcu.mpd.bgsdatastarter.repositories;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.database.EarthquakeDatabase;

public class EarthquakeRepository {

    private String DB_NAME = "earthquakes";

    private EarthquakeDatabase earthquakeDatabase;

    public EarthquakeRepository(Context ctx) {
        earthquakeDatabase = Room.databaseBuilder(ctx, EarthquakeDatabase.class, DB_NAME).build();
    }

    public void insertTasks(final List<Earthquake> earthquakes) {
       new AsyncTask<Void, Void, Void>() {
           @Override
           protected Void doInBackground(Void... voids) {
                earthquakeDatabase.earthquakeAccess().insertQuakes(earthquakes);
                return null;
           }
        };
    }

}
