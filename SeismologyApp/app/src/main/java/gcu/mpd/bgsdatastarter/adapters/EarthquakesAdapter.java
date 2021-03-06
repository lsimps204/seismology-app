package gcu.mpd.bgsdatastarter.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.activities.EarthquakeDetailActivity;
import gcu.mpd.bgsdatastarter.models.Earthquake;

/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */

public class EarthquakesAdapter extends RecyclerView.Adapter<EarthquakesAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "EarthquakeAdapter";

    // Holds list of earthquakes
    private List<Earthquake> earthquakes = new ArrayList<>();
    private List<Earthquake> earthquakesCopy;
    private TextView resultCount;
    private Context context;

    // Constructor: pass in the list of earthquakes and set local variable
    public EarthquakesAdapter(Context context) {
        this.context = context;
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView location;
        TextView magnitude;
        TextView depth;
        TextView date;
        TextView time;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            magnitude = itemView.findViewById(R.id.mag_textview);
            depth = itemView.findViewById(R.id.depth_textview);
            location = itemView.findViewById(R.id.location_textview);
            date = itemView.findViewById(R.id.date_textview);
            time = itemView.findViewById(R.id.time_textview);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public EarthquakesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View earthquakeView = inflater.inflate(R.layout.earthquake_listitem, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(earthquakeView);
        return viewHolder;
    }


    // The onBindViewHolder method populates data into the item through holder
    @Override
    public void onBindViewHolder(final EarthquakesAdapter.ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder called");

        // Get the data model based on position within the arraylist
        final Earthquake earthquake = earthquakes.get(position);

        // Set item views based on your views and data model

        viewHolder.location.setText(earthquake.getLocation().toString());
        viewHolder.magnitude.setText(Float.toString(earthquake.getMagnitude()));

        // set the colour of the background in circle displaying magnitude
        // colour depends on the value of the magnitude
        GradientDrawable magCircle = (GradientDrawable)viewHolder.magnitude.getBackground();
        magCircle.setColor(getMagnitudeColor(earthquake.getMagnitude()));

        // Set the data and time fields in the view
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM, y");
        viewHolder.date.setText(earthquake.getPubDate().toLocalDate().format(fmt));
        viewHolder.time.setText(earthquake.getPubDate().toLocalTime().toString());

        // Set the depth
        viewHolder.depth.setText("Depth: " +Integer.toString(earthquake.getDepth()) + "km");

        // Can set a click listener on each item here, for going into detail view, etc
        // More likely, can initiate a new activity for the detail view
        viewHolder.location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EarthquakeDetailActivity.class);
                intent.putExtra("title", earthquake.getTitle());
                intent.putExtra("location", earthquake.getLocation().toString());
                intent.putExtra("link", earthquake.getLink());
                intent.putExtra("magnitude", earthquake.getMagnitude());
                intent.putExtra("depth", earthquake.getDepth());
                intent.putExtra("date", earthquake.getPubDate().toLocalDate().format(fmt));
                intent.putExtra("time", earthquake.getPubDate().toLocalTime().toString());
                intent.putExtra("latitude", earthquake.getLocation().getCoordinates().getLat());
                intent.putExtra("longitude", earthquake.getLocation().getCoordinates().getLon());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {
        Log.e("COUNT: ", Integer.toString(earthquakes.size()));
        this.earthquakes = earthquakes;
        this.earthquakesCopy = new ArrayList<>(this.earthquakes);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Earthquake> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(earthquakesCopy);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Earthquake quake : earthquakesCopy) {
                    if (containsPattern(quake, filterPattern)) {
                        filteredList.add(quake);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            earthquakes.clear();
            earthquakes.addAll((List<Earthquake>) results.values);
            notifyDataSetChanged();
        }

        private boolean containsPattern(Earthquake quake, String filterPattern) {
            String location = quake.getLocation().toString().toLowerCase();
            return location.contains(filterPattern.toLowerCase());
        }
    };

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.mag1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.mag2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.mag3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.mag4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.mag5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.mag6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.mag7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.mag8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.mag9;
                break;
            default:
                magnitudeColorResourceId = R.color.mag10;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }
}
