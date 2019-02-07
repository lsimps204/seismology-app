package gcu.mpd.bgsdatastarter;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;

public interface MainActivityView {

    void displayEarthquakes(List<Earthquake> earthquakes);
}
