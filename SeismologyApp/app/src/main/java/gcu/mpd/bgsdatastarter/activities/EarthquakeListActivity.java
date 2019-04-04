package gcu.mpd.bgsdatastarter.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.adapters.EarthquakesAdapter;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

public class EarthquakeListActivity extends AppCompatActivity {

    private EarthquakeListViewModel viewModel;
    private EarthquakesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_list_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);

        // observe any changes to the list
        viewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                adapter.setEarthquakes(earthquakes);
                LocalDate localDate = LocalDate.of(2019, 03, 22);
                LocalDate localDate2 = LocalDate.of(2019, 04, 1);
                //List<Earthquake> quakes = viewModel.getEarthquakesByDate(localDate);
                List<Earthquake> quakes2 = viewModel.getEarthquakesBetweenDates(localDate, localDate2);
                //HashMap<String, Integer> map = viewModel.groupByHour();
                HashMap<String, Integer> map = viewModel.groupByDay();
                System.out.println("Qukess: " + quakes2);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.earthquake_recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new EarthquakesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
