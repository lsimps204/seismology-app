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
package gcu.mpd.bgsdatastarter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.network.EarthquakeXmlParser;
import gcu.mpd.bgsdatastarter.network.WebService;
import gcu.mpd.bgsdatastarter.repositories.EarthquakeRepository;
import gcu.mpd.bgsdatastarter.viewmodels.EarthquakeListViewModel;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private ProgressBar spinner;
    private EarthquakeListViewModel earthquakeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        startButton.setOnClickListener(this);

        // More Code goes here
        earthquakeListViewModel = ViewModelProviders.of(this).get(EarthquakeListViewModel.class);
        earthquakeListViewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                System.out.println(earthquakes);
            }
        });
    }

    // Button click handler - fetches the data from the API
    public void onClick(View aview)
    {
        spinner.setVisibility(View.VISIBLE);
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        //new Thread(new Task(urlSource)).start();
        ExecutorService service = Executors.newSingleThreadExecutor();
        WebService ws = new WebService();
        Future<String> future = service.submit(ws);
        String result;
        try {
            result = future.get();
            EarthquakeXmlParser xmlParser = new EarthquakeXmlParser(result);
            System.out.println(result.length());
            rawDataDisplay.setText(result);
        } catch (Exception e) {
            Log.e("EarthquakeRepository", "Error retrieving data");
        } finally {
            service.shutdown();
        }

    }

}