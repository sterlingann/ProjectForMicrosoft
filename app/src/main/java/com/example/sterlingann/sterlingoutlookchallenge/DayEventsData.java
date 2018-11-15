package com.example.sterlingann.sterlingoutlookchallenge;

import java.util.ArrayList;
import java.util.List;

// Container for all events (if any) of a particular day
class DayEventsData
{
    boolean isRegularEvent = false;
    boolean isNoEvent = false;
    List<Event> eventList = new ArrayList<Event>();
}
