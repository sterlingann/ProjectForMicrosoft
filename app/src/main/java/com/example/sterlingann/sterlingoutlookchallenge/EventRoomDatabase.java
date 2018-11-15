package com.example.sterlingann.sterlingoutlookchallenge;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * EventRoomDatabase. Includes code to create the database.
 * After the app creates the database, all further interactions
 * with it happen through the EventViewModel.
 */

@Database(entities = {Event.class}, version = 2, exportSchema = false)
public abstract class EventRoomDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

    private static EventRoomDatabase INSTANCE;

    static EventRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventRoomDatabase.class, "event_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // This callback is called when the database has opened.
    // PopulateDbAsync will populate the database
    // with the initial data set if the database has no entries.
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EventDao mDao;

        //Initial data
        static Calendar calendar = Calendar.getInstance();
        static int thisday = calendar.get(Calendar.DAY_OF_MONTH);
        static int month = calendar.get(Calendar.MONTH);
        static int year = calendar.get(Calendar.YEAR);
        static int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        private static Event reviewSterlingsCode = new Event("Review Sterling's Code", "please check it out", 1, dayOfWeek +1 , thisday + 1, thisday +1, month, month, year, year, 12, 12,1, 59);
        private static Event kindlyProvideSterlingFeedback = new Event("Kindly give Sterling feedback", "thank you!", 1, dayOfWeek +2 , thisday + 2, thisday +2, month, month, year, year, 12, 12,1, 59);
        private static Event thanksgiving = new Event("Thanksgiving", "Federal Holiday", 1,5, 22, 22, 10, 10, 2018, 2018,12,12,1,59);
        private static Event christmas = new Event("Christmas", "Federal Holiday", 1, 3, 25, 25, 11, 11, 2018, 2018,12,12,1,59);

        PopulateDbAsync(EventRoomDatabase db) {
            mDao = db.eventDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no events, then populate database with an initial list.
            if (mDao.getAnyEvent().length < 1) {
                mDao.insert(thanksgiving);
                mDao.insert(christmas);
                mDao.insert(reviewSterlingsCode);
                mDao.insert(kindlyProvideSterlingFeedback);
            }
            return null;
        }
    }
}


