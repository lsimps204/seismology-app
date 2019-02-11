package gcu.mpd.bgsdatastarter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import gcu.mpd.bgsdatastarter.R;
import gcu.mpd.bgsdatastarter.models.Earthquake;

public class EarthquakesAdapter extends RecyclerView.Adapter<EarthquakesAdapter.ViewHolder> {

    private static final String TAG = "EarthquakeAdapter";

    // Holds list of earthquakes
    private List<Earthquake> earthquakes;
    private Context context;

    // Constructor: pass in the list of earthquakes and set local variable
    public EarthquakesAdapter(Context context, List<Earthquake> earthquakes) {
        context = context;
        earthquakes = earthquakes;
    }



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        public TextView nameTextView;
//        public Button messageButton;
        TextView location;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            location = itemView.findViewById(R.id.quake_location);
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


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(EarthquakesAdapter.ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder called");

        // Get the data model based on position
        final Earthquake earthquake = earthquakes.get(position);

        // Set item views based on your views and data model
        viewHolder.location.setText(earthquake.getLocation().getTown() + " " + earthquake.getLocation().getCounty());

        // Can set a click listener on each item here, for going into detail view, etc
        // More likely, can initiate a new activity for the detail view
        viewHolder.location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, earthquake.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }
}
