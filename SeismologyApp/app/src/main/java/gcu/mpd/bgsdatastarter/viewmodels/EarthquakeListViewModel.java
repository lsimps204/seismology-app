package gcu.mpd.bgsdatastarter.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

public class EarthquakeListViewModel extends AndroidViewModel {

    private LiveData<List<Earthquake>> earthquakes;
    private EarthquakeRepository repository;

    public EarthquakeListViewModel(@NonNull Application app) {
        super(app);
        repository = new EarthquakeRepository(app);
        //earthquakes = repository.getAllEarthquakes();
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        return earthquakes;
    }

}
