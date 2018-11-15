package com.example.sterlingann.sterlingoutlookchallenge;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Entity class that represents a event in the database
 */

@Entity(tableName = "event_table")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    String mTitle;


    @NonNull
    @ColumnInfo(name = "description")
    String mDescription;

    @ColumnInfo(name = "isallday")
    int mIsAllDay;

    @ColumnInfo(name = "isalldaynumber")
    int mIsAllDayNumber;

    @ColumnInfo(name = "startday")
    int mStartDay;

    @ColumnInfo(name = "endday")
    int mEndDay;

    @ColumnInfo(name = "startmonth")
    int mStartMonth;

    @ColumnInfo(name = "endmonth")
    int mEndMonth;

    @ColumnInfo(name = "startyear")
    int mStartYear;

    @ColumnInfo(name = "endyear")
    int mEndYear;

    @ColumnInfo(name = "starthour")
    int mStartHour;

    @ColumnInfo(name = "endhour")
    int mEndHour;

    @ColumnInfo(name = "startminute")
    int mStartMinute;

    @ColumnInfo(name = "endminute")
    int mEndMinute;

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    String getmTitle() {
        return mTitle;
    }

    public void setmTitle(@NonNull String mTitle) {
        this.mTitle = mTitle;
    }
    @NonNull
    String getmDescription() {
        return mDescription;
    }

    int getmIsAllDay() {
        return mIsAllDay;
    }

    int getmIsAllDayNumber() {
        return mIsAllDayNumber;
    }

    int getmStartDay() {
        return mStartDay;
    }

    void setmStartDay(int mStartDay) {
        this.mStartDay = mStartDay;
    }

    int getmEndDay() {
        return mEndDay;
    }

    int getmStartMonth() {
        return mStartMonth;
    }

    void setmStartMonth(int mStartMonth) {
        this.mStartMonth = mStartMonth;
    }

    int getmEndMonth() {
        return mEndMonth;
    }

    int getmStartYear() {
        return mStartYear;
    }

    int getmEndYear() {
        return mEndYear;
    }

    void setmStartYear(int mStartYear){this.mStartYear = mStartYear;}

    int getmStartHour() {
        return mStartHour;
    }

    int getmEndHour() {
        return mEndHour;
    }

    int getmStartMinute() {
        return mStartMinute;
    }

    int getmEndMinute() {
        return mEndMinute;
    }

    public Event(@NonNull String title, @NonNull String description, int isAllDay, int isAllDayNumber, int startDay, int endDay, int startMonth, int endMonth, int startYear, int endYear, int startHour, int endHour, int startMinute, int endMinute) {
        this.mTitle = title;
        this.mDescription = description;
        this.mIsAllDay = isAllDay;
        this.mIsAllDayNumber = isAllDayNumber;
        this.mStartDay = startDay;
        this.mEndDay = endDay;
        this.mStartMonth = startMonth;
        this.mEndMonth = endMonth;
        this.mStartYear = startYear;
        this.mEndYear = endYear;
        this.mStartHour = startHour;
        this.mEndHour = endHour;
        this.mStartMinute = startMinute;
        this.mEndMinute = endMinute;
    }

    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */

    @Ignore
    public Event(int id, @NonNull String title, @NonNull String description, int isAllDay, int isAllDayNumber, int startDay, int endDay, int startMonth, int endMonth, int startYear, int endYear, int startHour, int endHour, int startMinute, int endMinute) {
        this.id = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mIsAllDay = isAllDay;
        this.mIsAllDayNumber = isAllDayNumber;
        this.mStartDay = startDay;
        this.mEndDay = endDay;
        this.mStartMonth = startMonth;
        this.mEndMonth = endMonth;
        this.mStartYear = startYear;
        this.mEndYear = endYear;
        this.mStartHour = startHour;
        this.mEndHour = endHour;
        this.mStartMinute = startMinute;
        this.mEndMinute = endMinute;

    }
}

