package gcu.mpd.bgsdatastarter.activities;

import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Mobile Platform Development Coursework 2019
 * Name:                    Lyle Simpson
 * Student ID:              S1436436
 * Programme of study:      Computing
 * 2019 April 11
 */

/* Fragment which shows the DatePicker widget */
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DatePickerFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: The fragment dialog has been created");
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, y, m, d);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "onDateSet:  Date choosen "+day+" "+month+" "+year);
        EarthquakeListActivity listView = (EarthquakeListActivity) getActivity();
        listView.filterDate(year, month, day);
    }
}
