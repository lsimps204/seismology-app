package gcu.mpd.bgsdatastarter.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
//    public int getNumberEntities() {
//        return earthquakes.getValue().size();
//    }

    public int getCount() {
        return repository.getCount();
    }
}
