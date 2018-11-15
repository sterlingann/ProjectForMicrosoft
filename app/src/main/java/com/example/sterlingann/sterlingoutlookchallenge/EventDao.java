package com.example.sterlingann.sterlingoutlookchallenge;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for a event.
 * Each method performs a database operation, such as inserting or deleting a event,
 * running a DB query, or deleting all events.
 */
@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Query("DELETE FROM event_table")
    void deleteAll();

    @Delete
    void deleteEvent(Event event);

    @Query("SELECT * from event_table LIMIT 1")
    Event[] getAnyEvent();

    @Query("SELECT * from event_table ORDER BY title ASC")
    LiveData<List<Event>> getAllEvents();

    @Update
    void update(Event... event);
}

