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

import gcu.mpd.bgsdatastarter.models.FeedMetadata;

public class EarthquakeXmlParser {

    private final String TAG = "EarthquakeXmlParser";
    private final String DETAIL_ITEM_TAG = "item";
    private String xml; // xml from the data source
    private XmlPullParser xpp;

    public EarthquakeXmlParser(String xml) {
        this.xml = xml;
    }

    /* Master method for parsing the XML string
    *  Calls submethods to do the processing and return domain objects */
    public void parse() {
        this.setupParser();

        if (this.xpp != null) {
            FeedMetadata feedbackMetadata = this.buildMetaData();
            this.buildEarthquakes();
        }
    }

    private void buildEarthquakes() {
        Log.d(TAG, "Building quakes");
        try {
            int eventType = this.xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && eventType != -1) {

                if(eventType == XmlPullParser.START_DOCUMENT) {
                    //System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    //System.out.println("Start tag "+xpp.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                    //System.out.println("End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    //System.out.println("Text "+xpp.getText());
                }

                // get next event
                eventType = this.toNextEvent();
            }
        } catch (XmlPullParserException ex) {
            Log.e(TAG, "Error building earthquake models from XML");
        }
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
                            if (lastDate == null) {
                                lastDate = this.parseToDateTime(this.xpp.getText());
                            }
                            break;
                    }
                }
            } catch(XmlPullParserException ex) {
                Log.e(TAG, "Error attaining event type when building metadata object");
            }

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
