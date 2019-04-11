package gcu.mpd.bgsdatastarter.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

public class EarthquakeStatisticsView {

    private String dateMostQuakes;
    private String hourMostQuakes;

    private String countyMostQuakes;
    private int deepestQuakeValue;
    private float highestMagnitudeValue;

    private int dateMostQuakesCount;
    private int hourMostQuakesCount;
    private int countyMostQuakesCount;
    private int quakeCount;

    private EarthquakeRepository repository;

    public EarthquakeStatisticsView(EarthquakeRepository repo) {
        this.repository = repo;
        this.makeStats(); // populates the statistics view on construction
    }

    private void makeStats() {
        deepestQuakeValue = repository.getDeepestQuake().getDepth();
        System.out.println("DEEEEP: " + Integer.toString(deepestQuakeValue));
        highestMagnitudeValue = repository.getHighestMagnitude().getMagnitude();
        dateMostQuakes = repository.getDayWithMostQuakes().getKey();
        dateMostQuakesCount = repository.getDayWithMostQuakes().getValue().size();
        countyMostQuakes = repository.getCountyWithMostQuakes().getKey();
        countyMostQuakesCount = repository.getCountyWithMostQuakes().getValue().size();
        hourMostQuakes = repository.hourWithMostQuakes().getKey();
        hourMostQuakesCount = repository.hourWithMostQuakes().getValue();
        quakeCount = repository.getEarthquakeCount();
    }


    /* GETTERS */
    public String getDateMostQuakes() {
        return dateMostQuakes;
    }

    public String getHourMostQuakes() {
        return hourMostQuakes;
    }

    public String getCountyMostQuakes() {
        return countyMostQuakes;
    }

    public int getDeepestQuakeValue() {
        return deepestQuakeValue;
    }

    public float getHighestMagnitudeValue() {
        return highestMagnitudeValue;
    }

    public int getDateMostQuakesCount() {
        return dateMostQuakesCount;
    }

    public int getHourMostQuakesCount() {
        return hourMostQuakesCount;
    }

    public int getCountyMostQuakesCount() {
        return countyMostQuakesCount;
    }

    public int getQuakeCount() {
        return quakeCount;
    }

}
