package gcu.mpd.bgsdatastarter.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

public class EarthquakeListViewModel extends ViewModel {

    private LiveData<List<Earthquake>> earthquakes;
    private EarthquakeRepository eRepo;

    public void init() {
        if (earthquakes != null) {
            return;
        }
        eRepo.getEarthquakes();
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        return earthquakes;
    }
}
