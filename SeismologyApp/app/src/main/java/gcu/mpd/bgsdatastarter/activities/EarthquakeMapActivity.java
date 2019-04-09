package gcu.mpd.bgsdatastarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;

import gcu.mpd.bgsdatastarter.R;

public class EarthquakeMapActivity extends AppCompatActivity {
    private static final String TAG = "EarthquakeMapActivity";

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_map);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);
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
                            break;

                        default:
                            break;
                    }

                    return false;
                }
            };
}
