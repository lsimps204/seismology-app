package gcu.mpd.bgsdatastarter.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

public class EarthquakeListViewModel extends AndroidViewModel {

    private LiveData<List<Earthquake>> earthquakes;
    private EarthquakeRepository repository;

    public EarthquakeListViewModel(@NonNull Application app) {
        super(app);
        repository = new EarthquakeRepository(app);
//        earthquakes = repository.getAllEarthquakes();
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        return repository.getAllEarthquakes();
    }

    public List<Earthquake> getEarthquakesByDate(LocalDate date) {
        return repository.getEarthquakesByDate(date);
    }

    public List<Earthquake> getEarthquakesBetweenDates(LocalDate d1, LocalDate d2){
        return repository.getEarthquakesBetweenDates(d1, d2);
    }

    /* Date ordering methods*/
    public List<Earthquake> orderedMostRecent() {
        return repository.orderByMostRecent();
    }

    public List<Earthquake> orderedLeastRecent() {
        List<Earthquake> quakesOrdered = repository.orderByMostRecent();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    /* Location ordering methods */
    public List<Earthquake> orderedByLocation() {
        return repository.orderByLocation();
    }

    public List<Earthquake> orderByLocationReverse() {
        List<Earthquake> quakesOrdered = repository.orderByLocation();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    /* Magnitude ordering methods */
    public List<Earthquake> orderByMagnitude() {
        List<Earthquake> quakesOrdered = repository.orderByMagnitude();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    public List<Earthquake> orderByMagnitudeReverse() {
        return repository.orderByMagnitude();
    }

    /* Depth ordering */
    public List<Earthquake> orderByDepth() {
        return repository.orderByDepth();
    }

    public List<Earthquake> orderByDepthReverse() {
        List<Earthquake> quakesOrdered = repository.orderByDepth();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    /* Coordinate ordering */
    public List<Earthquake> orderByMostNorthern() {
        return repository.orderByMostNorthern();
    }

    public List<Earthquake> orderByMostSouthern() {
        List<Earthquake> quakesOrdered = repository.orderByMostNorthern();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    public List<Earthquake> orderByMostWestern(){
        return repository.orderByMostWestern();
    }

    public List<Earthquake> orderByMostEastern(){
        List<Earthquake> quakesOrdered = repository.orderByMostWestern();
        Collections.reverse(quakesOrdered);
        return quakesOrdered;
    }

    public void refreshData() {
        repository.fetchRemoteData();
    }

    public Earthquake getHighestMagn(){
        return repository.getHighestMagnitude();
    }

    public HashMap<String,Integer> groupByHour(){
        return repository.earthquakesPerHour();
    }

    public HashMap<String, Integer> groupByDay() {
        return repository.earthquakesPerDay();
    }

    public void deleteAll() {
        repository.deleteAllEarthquakes();
    }

    public int getCount() {
        return repository.getCount();
    }

    public int deepestQuake() {
        return repository.getDeepestQuake().getDepth();
    }

    public String highestMag() {
        return Float.toString(repository.getHighestMagnitude().getMagnitude());
    }

    public Map.Entry<String, List<Earthquake>> dateMost() {
        return repository.getDayWithMostQuakes();
    }

    public Map.Entry<String, List<Earthquake>> countyMost() {
        return repository.getCountyWithMostQuakes();
    }

    public Map.Entry<String, Integer> hourMost() {
        return repository.hourWithMostQuakes();
    }

}
