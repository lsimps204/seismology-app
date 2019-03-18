package gcu.mpd.bgsdatastarter.models.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDateTime;

// converts dates to strings, and vice versa
public class DateConverter {
    // https://android.jlelse.eu/room-persistence-library-typeconverters-and-database-migration-3a7d68837d6c
    @TypeConverter
    public static LocalDateTime toDate(String date) {
        if (date == null) return null;
        else return LocalDateTime.parse(date);
    }

    @TypeConverter
    public static String toString(LocalDateTime date) {
        if (date == null) return null;
        else return date.toString();
    }
}