package gcu.mpd.bgsdatastarter.models.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import gcu.mpd.bgsdatastarter.models.Earthquake;
import gcu.mpd.bgsdatastarter.models.TypeConverters.DateConverter;
import gcu.mpd.bgsdatastarter.models.daos.EarthquakeDao;

@Database(entities={Earthquake.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class EarthquakeDatabase extends RoomDatabase {

    public abstract EarthquakeDao earthquakeDao();

    private static EarthquakeDatabase INSTANCE;

    // Creates Singleton instance of the database
    public static EarthquakeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EarthquakeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EarthquakeDatabase.class, "earthquake_database").build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDbAsyncTask(INSTANCE).execute(); // call the below AsyncTask to populate
        }
    };

//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private EarthquakeDao dao;
//
//        private PopulateDbAsyncTask(EarthquakeDatabase db) {
//            this.dao = db.earthquakeDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            // populate here
//            return null;
//        }
//    }

}
