package gcu.mpd.bgsdatastarter.network;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcu.mpd.bgsdatastarter.models.Coordinates;
import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.FeedMetadata;
import gcu.mpd.bgsdatastarter.models.Location;

/* This class parses the XML data from the API into domain objects */
public class EarthquakeXmlParser {

    private final String TAG = "EarthquakeXmlParser";
    private final String DETAIL_ITEM_TAG = "item";
    private String xml; // xml from the data source
    private XmlPullParser xpp;

    // containers
    private List<Earthquake> earthquakes;
    private FeedMetadata feedMetadata;

    // Constructor, which accepts the XML data to be parsed
    public EarthquakeXmlParser(String xml) {
        this.xml = xml;
        this.setupParser();
        this.parse();
    }

    /* Master method for parsing the XML string
    *  Calls submethods to do the processing and return domain objects
     */
    private void parse() {
        if (this.xpp != null) {
            this.feedMetadata = this.buildMetaData();
            this.earthquakes = this.buildEarthquakes();
        }
    }

    // getters
    public List<Earthquake> getEarthquakes() {
        return this.earthquakes;
    }
    public FeedMetadata getFeedMetadata() {
        return feedMetadata;
    }


    // Logic for building list of earthquakes from the XML data
    private List<Earthquake> buildEarthquakes() {
        Log.d(TAG, "Building quakes");
        List<Earthquake> eQuakes = new ArrayList<>();

        try {
            int eventType = this.xpp.getEventType();

            Earthquake earthquake  = null;
            Location location = null;
            Coordinates coords = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = null;

                if(eventType == XmlPullParser.START_TAG) {
                    tagName = this.xpp.getName();

                    if (tagName.equals("item")) {
                        earthquake  = new Earthquake();
                        location = new Location();
                        coords = new Coordinates();
                    } else if (earthquake != null) {
                        switch(tagName) {
                            case "title":
                                try {
                                    earthquake.setTitle(this.xpp.nextText());
                                } catch(XmlPullParserException ex) {
                                    Log.e(TAG, "Exception occurred");
                                } catch (IOException ex) {
                                    Log.e(TAG, "IO Exception occurred");
                                }
                                break;

                            case "description":
                                String[] descrip;
                                try {
                                    descrip = this.xpp.nextText().split(";");
                                    // Date of quake
                                    String[] splitDate = descrip[0].split(":");
                                    String[] datetimeArr = Arrays.copyOfRange(splitDate, 1, splitDate.length);
                                    String datetime = String.join(":", datetimeArr).trim();
                                    earthquake.setPubDate(parseToDateTime(datetime));

                                    // Location (town/county)
                                    String locationStr = descrip[1].split(":")[1].trim();
                                    if (locationStr.contains(",")) {
                                        String[] locSplit = locationStr.split(",");
                                        location.setTown(locSplit[0].trim());
                                        location.setCounty(locSplit[1].trim());
                                    } else {
                                        location.setTown(locationStr.trim());
                                    }

                                    // Lat/Lon coordinates
                                    String[] latlon = descrip[2].split(":")[1].split(",");
                                    coords.setLat(Float.parseFloat(latlon[0]));
                                    coords.setLon(Float.parseFloat(latlon[1]));
                                    location.setCoordinates(coords);

                                    // Depth
                                    String[] depthAndUnits = descrip[3].split(":")[1].trim().split(" ");
                                    int depth = Integer.parseInt(depthAndUnits[0].trim());
                                    String units = depthAndUnits[1].trim();
                                    earthquake.setDepth(depth);
                                    earthquake.setDepthUnit(units);

                                    // Magnitude
                                    float mag = Float.parseFloat(descrip[4].split(":")[1].trim());
                                    earthquake.setMagnitude(mag);

                                } catch(XmlPullParserException ex) {
                                    Log.e(TAG, "Exception occurred");
                                } catch (IOException ex) {
                                    Log.e(TAG, "IO Exception occurred");
                                }
                                break;

                            case "link":
                                try {
                                    earthquake.setLink(this.xpp.nextText());
                                } catch(XmlPullParserException ex) {
                                    Log.e(TAG, "Exception occurred");
                                } catch (IOException ex) {
                                    Log.e(TAG, "IO Exception occurred");
                                }
                                break;
                        }
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    // write the model to the list if </item>
                    tagName = this.xpp.getName();
                    if (tagName.equals("item") && earthquake != null) {
                        earthquake.setLocation(location);
                        eQuakes.add(earthquake);
                    }
                }

                // get next event
                eventType = this.toNextEvent();
            }
        } catch (XmlPullParserException ex) {
            Log.e(TAG, "Error building earthquake models from XML");
        }

        return eQuakes;
    }

    private FeedMetadata buildMetaData() {
        String title = "";
        String link = "";
        String description = "";
        LocalDateTime lastDate = null;

        while (this.xpp.getName() == null) {
            this.toNextEvent();
        }

        String tag = this.xpp.getName();

        while (!tag.equals(DETAIL_ITEM_TAG)) {
            try {
                if (this.xpp.getEventType() == XmlPullParser.TEXT) {
                    switch (tag) {
                        case "title":
                            if (title.equals("")) title = this.xpp.getText();
                            break;
                        case "link":
                            if (link.equals("")) {
                                link = this.xpp.getText();
                            }
                            break;
                        case "description":
                            if (description.equals("")) description = this.xpp.getText();
                            break;
                        case "lastBuildDate":
                            if (lastDate == null)
                                lastDate = this.parseToDateTime(this.xpp.getText());
                            break;
                    }
                }
            } catch(XmlPullParserException ex) {
                Log.e(TAG, "Error attaining event type when building metadata object");
            }

            // move the parser to the next event, and change the tag if necessary
            this.toNextEvent();
            if (this.xpp.getName() != null) {
                tag = this.xpp.getName();
            }
        }

        FeedMetadata feedMeta = new FeedMetadata(title, link, description, lastDate);
        return feedMeta;
    }

    private LocalDateTime parseToDateTime(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy H:m:s");
        return LocalDateTime.parse(datetime, formatter);
    }

    // Sets up the XmlPullParser object with the sourced XML
    private void setupParser() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            this.xpp = factory.newPullParser();
            this.xpp.setInput(new StringReader(this.xml));
        } catch(XmlPullParserException ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private int toNextEvent() {
        int eventType = -1;
        try {
            eventType = xpp.next(); // get next event
        } catch (IOException ex){
            Log.e(TAG, "Exception occurred while processing XML");
        } catch (XmlPullParserException ex) {
            Log.e(TAG, "Exception occured processing XML");
        }
        return eventType;
    }

}
