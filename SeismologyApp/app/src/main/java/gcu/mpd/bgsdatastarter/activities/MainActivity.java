/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 _________________
// Student ID           _________________
// Programme of Study   _________________
//

// Update the package name to include your Student Identifier
package gcu.mpd.bgsdatastarter.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private Button deleteButton;
    private ProgressBar spinner;
    private EarthquakeListViewModel earthquakeListViewModel;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        startButton = (Button)findViewById(R.id.startButton);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.INVISIBLE);

        startButton.setOnClickListener(this);

        //earthquakeListViewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                earthquakeListViewModel.deleteAll();
//            }
//        });
    }


    // Button click handler - fetches the data from the API
    public void onClick(View aview)
    {
        spinner.setVisibility(View.VISIBLE);
        Intent myIntent = new Intent(getBaseContext(), EarthquakeListActivity.class);
        myIntent.putExtra("firstRun", true);
        startActivity(myIntent);
    }

}