package com.example.sterlingann.sterlingoutlookchallenge;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

/**
 * This class holds the implementation code for the methods that interact with the database.
 * Using a repository allows us to group the implementation methods together,
 * and allows the EventViewModel to be a clean interface between the rest of the app
 * and the database.
 */

class EventsRepository {

    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

    EventsRepository(Application application) {
        EventRoomDatabase db = EventRoomDatabase.getDatabase(application);
        mEventDao = db.eventDao();
        mAllEvents = mEventDao.getAllEvents();
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    void insert(Event event) {
        new insertAsyncTask(mEventDao).execute(event);
    }

    void update(Event event)  {
        new updateEventAsyncTask(mEventDao).execute(event);
    }

    void deleteAll()  {
        new deleteAllEventsAsyncTask(mEventDao).execute();
    }

    // Must run off main thread
     void deleteEvent(Event event) {
        new deleteEventAsyncTask(mEventDao).execute(event);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a event into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventDao mAsyncTaskDao;

        insertAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all words from the database (does not delete the table).
     */
    private static class deleteAllEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteAllEventsAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single event from the database.
     */
    private static class deleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.deleteEvent(params[0]);
            return null;
        }
    }

    /**
     *  Updates a event in the database.
     */
    private static class updateEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        updateEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
