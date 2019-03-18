package gcu.mpd.bgsdatastarter.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeDao;
import gcu.mpd.bgsdatastarter.models.database.EarthquakeDatabase;
import gcu.mpd.bgsdatastarter.network.EarthquakeXmlParser;
import gcu.mpd.bgsdatastarter.network.WebService;

public class EarthquakeRepository {
    private final String MYTAG = "EarthquakeRepository";
    private EarthquakeDao earthquakeDao;
    private LiveData<List<Earthquake>> allEarthquakes;

    public EarthquakeRepository(Context ctx) {
        EarthquakeDatabase db = EarthquakeDatabase.getDatabase(ctx);
        earthquakeDao = db.earthquakeDao();
    }

    // Retrieves all the earthquakes
    public LiveData<List<Earthquake>> getAllEarthquakes() {
        if (this.getCount() <= 0) {
            this.fetchRemoteData();
        }
        return earthquakeDao.getAllEarthquakes();
    }

    public int getCount() {
        try {
            return (new countAsyncTask(earthquakeDao).execute().get());
        } catch(Exception e) {
            return -1;
        }
    }

    // This method initiates the remote call to the API to fetch the data
    // Uses the ExecutorService to run the callable WebService on a different Thread
    public void fetchRemoteData() {
        Log.e(MYTAG, "Fetching data from the remote API");
        ExecutorService service = Executors.newSingleThreadExecutor();
        WebService ws = new WebService();
        Future<String> future = service.submit(ws);
        try {
            String result = future.get();
            EarthquakeXmlParser xmlParser = new EarthquakeXmlParser(result);
            List<Earthquake> quakes = xmlParser.getEarthquakes();
            this.insertEarthquakes(quakes);
        } catch (Exception e) {
            Log.e("EarthquakeRepository", e.toString());
        } finally {
            service.shutdown();
        }
    }

    /* Inserts all earthquakes into the database */
    public void insertEarthquakes(final List<Earthquake> earthquakes) {
       Earthquake[] quakesArr = new Earthquake[earthquakes.size()];
       Earthquake[] quakes = earthquakes.toArray(quakesArr);
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

    /* Gets a count of the database entities */
    private static class countAsyncTask extends AsyncTask<Void, Void, Integer> {
        private EarthquakeDao dao;

        countAsyncTask(EarthquakeDao dao) { this.dao = dao; }
        @Override
        protected Integer doInBackground(Void... voids) {
            return dao.count();
        }
    }
}
