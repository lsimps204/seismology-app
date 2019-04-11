package gcu.mpd.bgsdatastarter.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */

/* This class is a Callable implementation that fetches data from the BGS XML API.
*  It is called and used within the EarthquakeRepository class, which initiates the network call */
public class WebService implements Callable<String> {
    private final String TAG = "WebService";
    private String dataSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    public WebService() {}

    @Override
    public String call() throws InvalidParameterException {
        Log.d(TAG, "call: Invoking web request on new thread");
        String result = "";
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        System.out.println("in run");

        try {
            Log.d(TAG, "in try");
            aurl = new URL(this.dataSource);
            yc = aurl.openConnection();

            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            // Skip first two lines (XML metadata)
            for (int i = 0; i < 2; i++) {
                in.readLine();
            }

            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.equals("</rss>")) // bit of a hack
                    result = result + inputLine;
            }
            in.close();
        } catch (IOException ae) {
            Log.e(TAG, ae.toString());
        }
        return result;
    }
}
