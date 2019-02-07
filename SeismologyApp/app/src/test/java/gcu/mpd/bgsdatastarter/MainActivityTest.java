package gcu.mpd.bgsdatastarter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void shouldPassEarthquakesToView() {

        MainActivityView view = new MockView();
        EarthquakeRepository earthquakeRepository = new MockEarthquakeRepository();

        MainActivityPresenter presenter = new MainActivityPresenter(view, earthquakeRepository);
        presenter.loadEarthquakes();

        Assert.assertEquals(true, ((MockView) view).passed);


    }


    /* Mock objects */
    private class MockView implements MainActivityView {

        boolean passed;

        @Override
        public void displayEarthquakes(List<Earthquake> earthquakes) {
            passed=true;
        }
    }

    private class MockEarthquakeRepository implements EarthquakeRepository {
        @Override
        public List<Earthquake> getBooks() {
            return null;
        }
    }

}