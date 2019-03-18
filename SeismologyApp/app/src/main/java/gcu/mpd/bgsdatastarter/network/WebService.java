package gcu.mpd.bgsdatastarter.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

public class WebService implements Callable<String> {
    private String dataSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    public WebService() {}

    @Override
    public String call() throws InvalidParameterException {
        String result = "";
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        System.out.println("in run");

        try {
            System.out.println("in try");
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
            System.out.println(ae.toString());
        }
        return result;
    }
}
