package gcu.mpd.bgsdatastarter.models;

import java.util.List

public class EarthquakeCollection 
{
    private List<Earthquake> earthquakes;
    
    public List<Earthquake> getEarthquakes() {
        return this.earthquakes;
    }

    public List<Earthquake> getEarthquakesByCounty(String county) {
        return this.earthquakes.stream()
            .filter(quake -> quake.location.county.equalsIgnoreCase(county))
            .collect(Collectors.toList())
    }
}