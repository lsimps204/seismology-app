package gcu.mpd.bgsdatastarter.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeDao;
import gcu.mpd.bgsdatastarter.models.database.EarthquakeDatabase;

public class EarthquakeRepository {

    private EarthquakeDao earthquakeDao;
    private LiveData<List<Earthquake>> allEarthquakes;

    public EarthquakeRepository(Context ctx) {
        EarthquakeDatabase db = EarthquakeDatabase.getDatabase(ctx);
        earthquakeDao = db.earthquakeDao();
        allEarthquakes = earthquakeDao.getAllEarthquakes();
    }

    /* Inserts all earthquakes into the database */
    public void insertEarthquakes(final List<Earthquake> earthquakes) {
       Earthquake[] quakes = (Earthquake[]) earthquakes.toArray();
       new insertAsyncTask(earthquakeDao).execute(quakes);
    }

    /* Deletes all earthquakes from the database */
    public void deleteAllEarthquakes() {
        new deleteAllAsyncTask(earthquakeDao).execute();
    }

    /* ASYNC TASK DEFINITIONS */
    private static class insertAsyncTask extends AsyncTask<Earthquake, Void, Void> {
        private EarthquakeDao dao;

        insertAsyncTask(EarthquakeDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final Earthquake... quakes) {
            dao.insertAll(quakes);
            return null;
        }
    }

    /* Deletes all earthquakes from the database */
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private EarthquakeDao dao;

        deleteAllAsyncTask(EarthquakeDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllEarthquakes();
            return null;
        }
    }
}
