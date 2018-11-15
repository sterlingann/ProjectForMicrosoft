package com.example.sterlingann.sterlingoutlookchallenge;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class EventComparator implements Comparator<Event> {
    /* Comparator to sort events for a particular day. First ALL-Day events
      are displayed, followed by other events sorted according to time
    */

        public int compare(Event a, Event b) {
            if (a.getmIsAllDay() == 1 && b.getmIsAllDay()==0)
                return -1;
            if (a.getmIsAllDay() ==0 && b.getmIsAllDay() ==1)
                return 1;
            if (a.getmIsAllDay()==1)
                return 0;

            Calendar first = new GregorianCalendar(a.getmStartYear(), a.getmStartMonth(), a.getmStartDay(), a.getmStartHour(), a.getmStartMinute());
            Calendar second = new GregorianCalendar(b.getmStartYear(), b.getmStartMonth(), b.getmEndDay(), b.getmStartHour(), b.getmStartMinute());

            if (first.before(second))
                return -1;
            if (second.before(first))
                return 1;
            return 0;
        }
    }

