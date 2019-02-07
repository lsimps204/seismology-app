package gcu.mpd.bgsdatastarter.models;

import java.util.List;
import java.util.stream.Collectors;

public class EarthquakeCollection 
{
    private List<Earthquake> earthquakes;
    
    public List<Earthquake> getEarthquakes() {
        return this.earthquakes;
    }

//    public List<Earthquake> getEarthquakesByCounty(String county) {
//        return this.earthquakes.stream()
//            .filter(quake -> quake.getLocation().getCounty().equalsIgnoreCase(county))
//            .collect(Collectors.toList());
//    }

}