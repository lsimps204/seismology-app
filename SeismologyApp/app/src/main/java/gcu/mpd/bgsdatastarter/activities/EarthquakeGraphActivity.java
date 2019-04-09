package gcu.mpd.bgsdatastarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import gcu.mpd.bgsdatastarter.R;

public class EarthquakeGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_graph);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = nav.getMenu();
        MenuItem mItem = menu.getItem(3);
        mItem.setChecked(true);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.nav_list:
                            Intent intent = new Intent(EarthquakeGraphActivity.this, EarthquakeListActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.nav_map:
                            Intent intent2 = new Intent(EarthquakeGraphActivity.this, EarthquakeMapActivity.class);
                            startActivity(intent2);
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