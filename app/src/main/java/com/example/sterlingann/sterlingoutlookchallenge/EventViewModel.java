package com.example.sterlingann.sterlingoutlookchallenge;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * The EventViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class EventViewModel extends AndroidViewModel {

    private EventsRepository mRepository;

    private LiveData<List<Event>> mAllEvents;

    public EventViewModel(Application application) {
        super(application);
        mRepository = new EventsRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    void insert(Event event) {
        mRepository.insert(event);
    }

    void deleteAll() {
        mRepository.deleteAll();
    }

    void deleteEvent(Event event) {
        mRepository.deleteEvent(event);
    }

    void update(Event event) {
        mRepository.update(event);
    }
}
