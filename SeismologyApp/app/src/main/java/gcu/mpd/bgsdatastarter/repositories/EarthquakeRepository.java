package gcu.mpd.bgsdatastarter.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeDao;
import gcu.mpd.bgsdatastarter.models.database.EarthquakeDatabase;
import gcu.mpd.bgsdatastarter.network.EarthquakeXmlParser;
import gcu.mpd.bgsdatastarter.network.WebService;

/* The Repository class mediates between the ViewModel on the front-end, and the data sources on the back end.
*  The repository hides data fetching details, from both the SQLite database, and the remote XML API */
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
        this.allEarthquakes = earthquakeDao.getAllEarthquakes();
        return this.allEarthquakes;
    }

    public List<Earthquake> getEarthquakesByDate(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToCheck = date.format(fmt);
        List<Earthquake> quakesOnDate = new ArrayList<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            if (quake.getPubDate().toLocalDate().toString().equals(dateToCheck)) {
                quakesOnDate.add(quake);
            }
        }
        return quakesOnDate;
    }

    public List<Earthquake> getEarthquakesBetweenDates(LocalDate start, LocalDate end) {
        //DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = start.minusDays(1);
        LocalDate endDate = end.plusDays(1);
        List<Earthquake> quakesBetweenDates = new ArrayList<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            LocalDate quakeDate = quake.getPubDate().toLocalDate();
            if (quakeDate.isAfter(startDate) && quakeDate.isBefore(endDate)) {
                quakesBetweenDates.add(quake);
            }
        }
        return quakesBetweenDates;
    }

    public Earthquake getHighestMagnitude() {
        Comparator<Earthquake> cmp = new Comparator<Earthquake>() {
            @Override
            public int compare(Earthquake e1, Earthquake e2) {
                Float mag1 = e1.getMagnitude();
                Float mag2 = e2.getMagnitude();
                return mag1.compareTo(mag2);
            }
        };
        return Collections.max(this.allEarthquakes.getValue(), cmp);
    }

    public List<Earthquake> getEarthquakesByMagnitudeAbove(float magnitude) {
        List<Earthquake> quakes = new ArrayList<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            if (quake.getMagnitude() > magnitude) {
                quakes.add(quake);
            }
        }
        return quakes;
    }

    public List<Earthquake> findEarthquakesByName(String name) {
        List<Earthquake> quakes = new ArrayList<>();
        String searchTerm = name.toLowerCase();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            String county = quake.getLocation().getCounty().toLowerCase();
            String town = quake.getLocation().getTown().toLowerCase();
            if (county.contains(searchTerm) || town.contains(searchTerm)) {
                quakes.add(quake);
            }
        }
        return quakes;
    }
    
    public List<Earthquake> getEarthquakesByDepthAbove(int depth) {
        List<Earthquake> quakes = new ArrayList<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            if (quake.getDepth() > depth) {
                quakes.add(quake);
            }
        }
        return quakes;
    }

    public Earthquake getDeepestQuake() {
        Comparator<Earthquake> cmp = new Comparator<Earthquake>() {
            @Override
            public int compare(Earthquake e1, Earthquake e2) {
                Integer mag1 = e1.getDepth();
                Integer mag2 = e2.getDepth();
                return mag1.compareTo(mag2);
            }
        };
        return Collections.max(this.allEarthquakes.getValue(), cmp);
    }

    public Map.Entry<String, List<Earthquake>> getDayWithMostQuakes() {
        HashMap<String, List<Earthquake>> groupedQuakes = this.groupEarthquakesByDate();
        return this.findMostCommon(groupedQuakes);
    }


    public Map.Entry<String, List<Earthquake>> getCountyWithMostQuakes() {
        HashMap<String, List<Earthquake>> groupedQuakes = this.groupEarthquakesByCounty();
        return this.findMostCommon(groupedQuakes);
    }

    private Map.Entry<String, List<Earthquake>> findMostCommon(HashMap<String, List<Earthquake>> groupedQuakes) {
        Map.Entry<String, List<Earthquake>> maxEntry = null;

        for (Map.Entry<String, List<Earthquake>> entry : groupedQuakes.entrySet()) {
            if (maxEntry == null || entry.getValue().size() > maxEntry.getValue().size()) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    private HashMap<String, List<Earthquake>> groupEarthquakesByDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HashMap<String, List<Earthquake>> quakesByDate = new HashMap<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            String quakeDate = quake.getPubDate().toLocalDate().format(fmt);
            List<Earthquake> quakes = quakesByDate.get(quakeDate);
            if (quakes == null) {
                quakes = new ArrayList<>();
            }
            quakes.add(quake);
            quakesByDate.put(quakeDate, quakes);
        }
        return quakesByDate;
    }

    public HashMap<String, Integer> earthquakesPerHour() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:M:s");
        HashMap<String, Integer> countPerHour = new HashMap<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            String quakeDate = quake.getPubDate().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString();
            int count = countPerHour.getOrDefault(quakeDate, 0);
            count++;
            countPerHour.put(quakeDate, count);
        }
        return countPerHour;
    }

    public HashMap<String, Integer> earthquakesPerDay() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HashMap<String, Integer> countPerDay = new HashMap<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            String quakeDate = quake.getPubDate().toLocalDate().toString();
            int count = countPerDay.getOrDefault(quakeDate, 0);
            count++;
            countPerDay.put(quakeDate, count);
        }
        return countPerDay;
    }

    private HashMap<String, List<Earthquake>> groupEarthquakesByCounty() {
        HashMap<String, List<Earthquake>> quakesByCounty = new HashMap<>();
        for (Earthquake quake : this.allEarthquakes.getValue()) {
            String county = quake.getLocation().getCounty();
            if (county == null) continue;
            List<Earthquake> quakes = quakesByCounty.get(county);
            if (quakes == null) {
                quakes = new ArrayList<>();
            }
            quakes.add(quake);
            quakesByCounty.put(county, quakes);
        }
        return quakesByCounty;
    }

    public int getCount() {
        try {
            return (new countAsyncTask(earthquakeDao).execute().get());
        } catch(Exception e) {
            return -1;
        }
    }

    // This method initiates the remote call to the API to fetch the data
    // Uses the ExecutorService to run the callable WebService on a new Thread
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

    /***************************
     * ASYNC TASK DEFINITIONS
     */
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
