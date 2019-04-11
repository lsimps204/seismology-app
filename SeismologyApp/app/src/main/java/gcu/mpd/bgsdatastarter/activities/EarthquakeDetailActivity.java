package gcu.mpd.bgsdatastarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.format.DateTimeFormatter;

import gcu.mpd.bgsdatastarter.R;

/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */

/* Activity which shows details of an individual earthquake */
public class EarthquakeDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "EarthquakeDetailActivity";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MapView mapView;
    private TextView magnitudeView;

    private float magnitude;
    private double quakeLatitude;
    private double quakeLongitude;
    private String location;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_detail); //change

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);

        TextView locationView = findViewById(R.id.tvLocation);
        magnitudeView = findViewById(R.id.tvMagnitude);
        TextView dateView = findViewById(R.id.tvPubDate);
        TextView timeView = findViewById(R.id.tvTime);
        TextView depthView = findViewById(R.id.tvDepth);
        TextView coordsView = findViewById(R.id.tvCoordinates);
        TextView linkView = findViewById(R.id.tvLink);

        Bundle passedDataBundle = getIntent().getExtras();
        location = passedDataBundle.getString("location");
        float magnitude = passedDataBundle.getFloat("magnitude");
        date = passedDataBundle.getString("date");
        String time = passedDataBundle.getString("time");
        int depth = passedDataBundle.getInt("depth");
        String link = passedDataBundle.getString("link");
        quakeLatitude = passedDataBundle.getFloat("latitude");
        quakeLongitude = passedDataBundle.getFloat("longitude");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM, y");
        // set text values
        locationView.setText(location);
        magnitudeView.setText("Magnitude: " + Float.toString(magnitude));
        depthView.setText("Depth: " + Integer.toString(depth) + " km");
        dateView.setText("Date: " + date);
        timeView.setText("Time: " + time);
        linkView.setText("Link: " + link);
        coordsView.setText(String.format("Coordinates: %.2f, %.2f", quakeLatitude, quakeLongitude));

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady called");
        GoogleMap gmap = googleMap;
        gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json));
        gmap.setMinZoomPreference(7);
        LatLng position = new LatLng(quakeLatitude, quakeLongitude);

        gmap.moveCamera(CameraUpdateFactory.newLatLng(position));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position)
                .title(location)
                .snippet(date);;
        gmap.addMarker(markerOptions);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
                            Intent a = new Intent(EarthquakeDetailActivity.this, EarthquakeListActivity.class);
                            startActivity(a);
                            break;

                        case R.id.nav_map:
                            Intent intent2 = new Intent(EarthquakeDetailActivity.this, EarthquakeMapActivity.class);
                            startActivity(intent2);
                            break;

                        case R.id.nav_stats:
                            Intent intent3 = new Intent(EarthquakeDetailActivity.this, EarthquakeStatisticsActivity.class);
                            startActivity(intent3);
                            break;

                        case R.id.nav_graph:
                            Intent intent4 = new Intent(EarthquakeDetailActivity.this, EarthquakeGraphActivity.class);
                            startActivity(intent4);
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            };

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
}
