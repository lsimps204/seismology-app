package gcu.mpd.bgsdatastarter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.EarthquakeStatisticsView;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

public class EarthquakeStatisticsActivity extends AppCompatActivity {
    private final String TAG = "EarthquakeStatsActivity";
    private EarthquakeListViewModel viewModel;

    TextView tvCount;
    TextView tvDeepest;
    TextView tvMag;
    TextView tvDateMost;
    TextView tvCountyMost;
    TextView tvHourMost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_stats);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = nav.getMenu();
        MenuItem mItem = menu.getItem(3);
        mItem.setChecked(true);

        tvCount = findViewById(R.id.stat_quakecount);
        tvDeepest = findViewById(R.id.stat_deepest_quake);
        tvMag = findViewById(R.id.stat_hi_magnitude);
        tvDateMost = findViewById(R.id.stat_datemost);
        tvCountyMost = findViewById(R.id.stat_county_most);
        tvHourMost = findViewById(R.id.stat_hour_most);

        viewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);
        viewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                tvCount.setText("Number of earthquakes: " + Integer.toString(viewModel.getCount()));
                tvDeepest.setText("Deepest earthquake: " + viewModel.deepestQuake() + " km");
                tvMag.setText("Highest magnitude earthquake: " + viewModel.highestMag());
                String date = getReadableDate(viewModel.dateMost().getKey());
                String dateNum = Integer.toString(viewModel.dateMost().getValue().size());
                tvDateMost.setText("Date with most earthquakes: " + date + " (with " + dateNum + " earthquakes)");
                String county = viewModel.countyMost().getKey();
                String countyNum = Integer.toString(viewModel.countyMost().getValue().size());
                tvCountyMost.setText("County with most earthquakes: " + county + " (with " + countyNum + " earthquakes");

                String hourMost = formatHourString(viewModel.hourMost().getKey());
                String hourNum = viewModel.hourMost().getValue().toString();
                tvHourMost.setText("Hour with most earthquakes: " + hourMost + " (with " + hourNum + " earthquakes");
            }
        });
    }

    private String getReadableDate(String dateAsStr) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM, y");
        String[] components = dateAsStr.split("-");
        LocalDate dateMost = LocalDate.of(Integer.parseInt(components[0]), Integer.parseInt(components[1]), Integer.parseInt(components[2]));
        String displayDate= dateMost.format(fmt);
        return displayDate;
    }

    private String formatHourString(String hour) {
        LocalTime time = LocalTime.parse(hour);
        int hourStart = time.getHour();
        int hourEnd = time.plus(1, ChronoUnit.HOURS).getHour();
        String timeUnit = hourStart < 12 ? "am" : "pm";
        return String.format("%d%s-%d%s", hourStart, timeUnit, hourEnd, timeUnit);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
                            Intent intent1 = new Intent(EarthquakeStatisticsActivity.this, EarthquakeListActivity.class);
                            startActivity(intent1);
                            break;

                        case R.id.nav_map:
                            Intent intent2 = new Intent(EarthquakeStatisticsActivity.this, EarthquakeMapActivity.class);
                            startActivity(intent2);
                            break;

                        case R.id.nav_stats:
                            break;

                        case R.id.nav_graph:
                            Intent intent3 = new Intent(EarthquakeStatisticsActivity.this, EarthquakeGraphActivity.class);
                            startActivity(intent3);
                            break;

                    }

                    return false;
                }
            };
}
