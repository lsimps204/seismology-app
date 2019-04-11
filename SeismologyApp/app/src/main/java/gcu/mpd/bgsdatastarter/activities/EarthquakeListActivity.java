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

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.adapters.EarthquakesAdapter;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
//                            Intent i = new Intent(EarthquakeListActivity.this, EarthquakeListActivity.class);
                            break;

                        case R.id.nav_map:
                            Intent intent = new Intent(EarthquakeListActivity.this, EarthquakeMapActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.nav_search:
                            break;

                        case R.id.nav_graph:
                            Intent intent4 = new Intent(EarthquakeListActivity.this, EarthquakeGraphActivity.class);
                            startActivity(intent4);
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            };

    public void filterDate(int year, int month, int day) {
        Log.d(TAG, "filterDate: "+day+" "+month+" "+year);
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
        }
        return false;
    }

    private void displayPicker() {
        DialogFragment frag = new DateFragment();
        frag.show(getSupportFragmentManager(), getString(R.string.datepicker));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
