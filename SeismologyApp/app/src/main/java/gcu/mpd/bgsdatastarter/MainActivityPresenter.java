package gcu.mpd.bgsdatastarter;

import android.view.View;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

public class MainActivityPresenter {

    private MainActivityView view;
    private EarthquakeRepository earthquakeRepository;

    public MainActivityPresenter(MainActivityView view, EarthquakeRepository earthquakeRepository) {
        this.view = view;
        this.earthquakeRepository = earthquakeRepository;
    }


    public void loadEarthquakes() {
        List<Earthquake> quakes = earthquakeRepository.getBooks();
        // call view method
    }
}
