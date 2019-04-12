package gcu.mpd.bgsdatastarter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.adapters.EarthquakesAdapter;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */
public class EarthquakeListActivity extends AppCompatActivity {

    private final String TAG = "EarthquakeListActivity";
    private EarthquakeListViewModel viewModel;
    private EarthquakesAdapter adapter;
    private RecyclerView recyclerView;
    private DatePicker dPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_list_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.earthquake_recyclerview);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = nav.getMenu();
        MenuItem mItem = menu.getItem(0);
        mItem.setChecked(true);

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);

        Bundle passedDataBundle = getIntent().getExtras();
        if (passedDataBundle != null && passedDataBundle.containsKey("firstRun")) {
            viewModel.deleteAll();
            viewModel.refreshData();
        }
        // observe any changes to the list
        viewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                adapter.setEarthquakes(earthquakes);
            }
        });

        // instantiate the automatic refresh every 5 minutes
        setTimer();
    }

    /* This function refreshes the API data every five minutes */
    private void setTimer() {
        Timer timer = new Timer();
        int FIVEMINS = 1000 * 60 * 5;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewModel.refreshData();
            }
        }, 0,FIVEMINS);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
                            break;

                        case R.id.nav_map:
                            Intent intent = new Intent(EarthquakeListActivity.this, EarthquakeMapActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.nav_stats:
                            Intent intent2 = new Intent(EarthquakeListActivity.this, EarthquakeStatisticsActivity.class);
                            startActivity(intent2);
                            break;
                    }

                    return false;
                }
            };

    public void filterDate(int year, int month, int day) {
        Log.d(TAG, "filterDate: filtering the date on: "+day+" "+month+" "+year);
        LocalDate date = LocalDate.of(year, month+1, day);
        String dateAsString = date.format(DateTimeFormatter.ofPattern("d MMMM, y"));
        List<Earthquake> quakes = viewModel.getEarthquakesByDate(date);

        if(quakes.size() == 0){
            Toast.makeText(this, "No earthquakes found for: "+ dateAsString, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "processDatePickerResult: No results found on "+ date);
        }
        else{
            adapter.setEarthquakes(quakes);
        }
        Log.d(TAG, "processDatePickerResult: End");
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new EarthquakesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.datepicker:
                Log.d(TAG, "onOptionsItemSelected: datepicker chosen");
                displayPicker();
                break;
            case R.id.action_most_recent:
                Log.d(TAG, "onOptionsItemSelected: most recent ordering chosen");
                adapter.setEarthquakes(viewModel.orderedMostRecent());
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_least_recent:
                Log.d(TAG, "onOptionsItemSelected: least recent ordering chosen");
                adapter.setEarthquakes(viewModel.orderedLeastRecent());
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_alphabetical:
                Log.d(TAG, "onOptionsItemSelected: alphabetical ordering by location chosen");
                adapter.setEarthquakes(viewModel.orderedByLocation());
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_alphabetical_rev:
                Log.d(TAG, "onOptionsItemSelected: reverse alphabetical ordering by location chosen");
                adapter.setEarthquakes(viewModel.orderByLocationReverse());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_highest_mag:
                Log.d(TAG, "onOptionsItemSelected: highest magnitude ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMagnitude());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_lowest_mag:
                Log.d(TAG, "onOptionsItemSelected: lowest magnitude ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMagnitudeReverse());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_highest_depth:
                Log.d(TAG, "onOptionsItemSelected: highest depth ordering chosen");
                adapter.setEarthquakes(viewModel.orderByDepthReverse());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_lowest_depth:
                Log.d(TAG, "onOptionsItemSelected: lowest depth ordering chosen");
                adapter.setEarthquakes(viewModel.orderByDepth());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_most_northern:
                Log.d(TAG, "onOptionsItemSelected: most northern ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMostNorthern());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_most_southern:
                Log.d(TAG, "onOptionsItemSelected: most southern ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMostSouthern());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_most_western:
                Log.d(TAG, "onOptionsItemSelected: most western ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMostWestern());
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_most_eastern:
                Log.d(TAG, "onOptionsItemSelected: most eastern ordering chosen");
                adapter.setEarthquakes(viewModel.orderByMostEastern());
                adapter.notifyDataSetChanged();
                break;

            case R.id.refresh_data:
                Log.d(TAG, "onOptionsItemSelected: Refreshing data from API");
                viewModel.refreshData();
                break;
        }
        return false;
    }

    private void displayPicker() {
        DialogFragment frag = new DateFragment();
        frag.show(getSupportFragmentManager(), getString(R.string.datepicker));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: creating the options menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_placeholder));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null, 0);
                return false;
            }
        });
        return true;
    }

}
