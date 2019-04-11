package gcu.mpd.bgsdatastarter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.format.DateTimeFormatter;
import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.models.Coordinates;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.Location;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;


/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */

/* Activity which shows all earthquakes on a map */
public class EarthquakeMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "EarthquakeMapActivity";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private EarthquakeListViewModel viewModel;
    private MapView mapView;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_map);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = nav.getMenu();
        MenuItem mItem = menu.getItem(1);
        mItem.setChecked(true);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        viewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        mGoogleMap.setMinZoomPreference(5);
        LatLng glasgow = new LatLng(55.8642, -4.2518);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 4.0f));

        viewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                setMapMarkers(earthquakes);
            }
        });
    }

    private void setMapMarkers(List<Earthquake> earthquakes) {
        for (Earthquake quake : earthquakes) {
            Coordinates location = quake.getLocation().getCoordinates();
            LatLng position = new LatLng(location.getLat(), location.getLon());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM, y");
            String date = quake.getPubDate().toLocalDate().format(fmt);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(position)
                .title(quake.getLocation().toString())
                .snippet(date);
            mGoogleMap.addMarker(markerOptions);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
                            Intent intent = new Intent(EarthquakeMapActivity.this, EarthquakeListActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.nav_map:
                            break;

                        case R.id.nav_stats:
                            Intent intent2 = new Intent(EarthquakeMapActivity.this, EarthquakeStatisticsActivity.class);
                            startActivity(intent2);
                            break;

                        case R.id.nav_graph:
                            Intent intent3 = new Intent(EarthquakeMapActivity.this, EarthquakeGraphActivity.class);
                            startActivity(intent3);
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            };
}
