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

import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.models.Coordinates;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.Location;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

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
        mGoogleMap.setMinZoomPreference(6);

        viewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                setMapMarkers(earthquakes);
            }
        });
//        LatLng position = makeLatLng(geoLat,geoLong);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(position);
//        mGoogleMap.addMarker(markerOptions);

    }

    private void addMarker(Coordinates coords) {
        LatLng position = new LatLng(coords.getLat(), coords.getLon());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        mGoogleMap.addMarker(markerOptions);
    }

    private void setMapMarkers(List<Earthquake> earthquakes) {
        for (Earthquake quake : earthquakes) {
            Coordinates location = quake.getLocation().getCoordinates();
            addMarker(location);
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

                        case R.id.nav_search:
                            break;

                        case R.id.nav_graph:
                            Intent intent4 = new Intent(EarthquakeMapActivity.this, EarthquakeGraphActivity.class);
                            startActivity(intent4);
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            };
}
