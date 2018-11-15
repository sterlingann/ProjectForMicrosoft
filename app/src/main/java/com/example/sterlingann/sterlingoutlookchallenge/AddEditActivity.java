package com.example.sterlingann.sterlingoutlookchallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_DESCRIPTION;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_END_DAY;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_END_HOUR;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_END_MINUTE;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_END_MONTH;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_END_YEAR;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_ID;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_IS_ALL_DAY;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_NEW_EVENT;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_START_DAY;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_START_HOUR;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_START_MINUTE;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_START_MONTH;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_START_YEAR;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_TITLE;
import static com.example.sterlingann.sterlingoutlookchallenge.MainActivity.EXTRA_DATA_UPDATE_EVENT;

public class AddEditActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_RESPONSE_TYPE = "com.example.sterlingann.sterlingoutlookchallenge.EXTRA_REPLY_RESPONSE_TYPE";
    public static final String EXTRA_REPLY_TITLE = "com.example.sterlingann.sterlingoutlookchallenge.EXTRA_REPLY_TITLE";
    public static final String EXTRA_REPLY_DESCRIPTION = "com.example.sterlingann.sterlingoutlookchallenge.EXTRA_REPLY_DESCRIPTION";
    public static final String EXTRA_REPLY_IS_ALL_DAY = "com.example.sterlingann.sterlingoutlookchallenge.AddEditActivity.EXTRA_REPLY_IS_ALL_DAY";
    public static final String EXTRA_REPLY_IS_ALL_DAY_NUMBER = "com.example.sterlingann.sterlingoutlookchallenge.IS_ALL_DAY_NUMBER";
    public static final String EXTRA_REPLY_START_DAY = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_START_DAY";
    public static final String EXTRA_REPLY_END_DAY = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_END_DAY";
    public static final String EXTRA_REPLY_START_MONTH = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_START_MONTH";
    public static final String EXTRA_REPLY_END_MONTH = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_END_MONTH";
    public static final String EXTRA_REPLY_START_HOUR = "com.example.sterlingann.sterlingoutlookchallenge.START_HOUR";
    public static final String EXTRA_REPLY_END_HOUR = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_END_HOUR";
    public static final String EXTRA_REPLY_START_MINUTE = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_START_MINUTE";
    public static final String EXTRA_REPLY_END_MINUTE = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_END_MINUTE";
    public static final String EXTRA_REPLY_START_YEAR = "com.example.sterlingann.sterlingoutlookchallenge.REPLY_START_YEAR";
    public static final String EXTRA_REPLY_END_YEAR = "com.example.steringann.sterlingoutlookchallenge.REPLY_END_YEAR";
    TextDatePicker datePicker1;
    TextDatePicker datePicker2;

    TextTimePicker timePicker1;
    TextTimePicker timePicker2;
    TextView time1;
    TextView time2;

    private boolean mIsUpdateEvent = false;
    private boolean mIsAddEvent = false;
    private String mTitle, mDescription;
    private int mStartDay, mStartMonth, mStartYear, mStartHour, mStartMinute, mEndDay, mEndMonth, mEndYear, mEndHour, mEndMinute;
    private int mIsAllDay = 0;
    private static final int NO_ERR = 0;
    private static final int ERR_START_AFTER_END = 1;
    private static final int ERR_TIME_MORE_24 = 2;
    private static final int SECONDS_IN_A_DAY = 86400;
    private Bundle mExtras = new Bundle();
    int mRequestCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setTitle("Add or Edit Event");

        mExtras = getIntent().getExtras();


        // If we are passed content, fill it in for the user to edit.
        if (mExtras != null) {
            mIsAddEvent = mExtras.getBoolean(EXTRA_DATA_NEW_EVENT);
            mIsUpdateEvent = mExtras.getBoolean(EXTRA_DATA_UPDATE_EVENT);

            if(mIsUpdateEvent) {

                mRequestCode = MainActivity.UPDATE_EVENT_ACTIVITY_REQUEST_CODE;
                mTitle = mExtras.getString(EXTRA_DATA_TITLE);
                mDescription = mExtras.getString(EXTRA_DATA_DESCRIPTION);
                mIsAllDay = mExtras.getInt(EXTRA_DATA_IS_ALL_DAY);

                mStartDay = mExtras.getInt(EXTRA_DATA_START_DAY);
                mEndDay = mExtras.getInt(EXTRA_DATA_END_DAY);
                mStartMonth = mExtras.getInt(EXTRA_DATA_START_MONTH);
                mEndMonth = mExtras.getInt(EXTRA_DATA_END_MONTH);
                mStartYear = mExtras.getInt(EXTRA_DATA_START_YEAR);
                mEndYear = mExtras.getInt(EXTRA_DATA_END_YEAR);
                mStartHour = mExtras.getInt(EXTRA_DATA_START_HOUR);
                mEndHour = mExtras.getInt(EXTRA_DATA_END_HOUR);
                mStartMinute = mExtras.getInt(EXTRA_DATA_START_MINUTE);
                mEndMinute = mExtras.getInt(EXTRA_DATA_END_MINUTE);
            } else if (mIsAddEvent) {
                mRequestCode = MainActivity.NEW_EVENT_ACTIVITY_REQUEST_CODE;
                mStartDay = mExtras.getInt(EXTRA_DATA_START_DAY);
                mStartMonth = mExtras.getInt(EXTRA_DATA_START_MONTH);
                mStartYear = mExtras.getInt(EXTRA_DATA_START_YEAR);
                mStartHour = mExtras.getInt(EXTRA_DATA_START_HOUR);
                mStartMinute = mExtras.getInt(EXTRA_DATA_START_MINUTE);
            }
        }
        setDateTime();

        // Fill up the details (if any available), otherwise fields will be empty
        if (mIsUpdateEvent)
            fillForm();
        handleSwitchChange();
    }

    // Set the Date and Time field. Also add Date & Time picker listeners on the text views
    private void setDateTime() {
        int day, month, year, dayOfWeek, hour, minute;
        Calendar cal;
        if (!mIsUpdateEvent && !mIsAddEvent)
            cal = Calendar.getInstance();
        else
            cal = new GregorianCalendar(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        TextView date1 = findViewById(R.id.date1);
        date1.setText(DateTimeUtils.formattedDate(this, dayOfWeek, month, day, year));
        time1 =  findViewById(R.id.time1);
        time1.setText((DateTimeUtils.formattedTime(hour, minute)));
        datePicker2 = new TextDatePicker(this, R.id.date2, null);
        datePicker1 = new TextDatePicker(this, R.id.date1, datePicker2);
        datePicker1.day = day;
        datePicker1.month = month;
        datePicker1.year = year;

        timePicker2 = new TextTimePicker(this, R.id.time2, null);
        timePicker1 = new TextTimePicker(this, R.id.time1, timePicker2);
        timePicker1.hour = hour;
        timePicker1.minute = minute;

        if (mIsUpdateEvent) {
            cal = new GregorianCalendar(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute);
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
        }

        TextView date2 = findViewById(R.id.date2);
        date2.setText(DateTimeUtils.formattedDate(this, dayOfWeek, month, day, year));
        time2 = findViewById(R.id.time2);
        time2.setText((DateTimeUtils.formattedTime(hour, minute)));

        datePicker2.day = day;
        datePicker2.month = month;
        datePicker2.year = year;

        timePicker2.hour = hour;
        timePicker2.minute = minute;

        if (mIsUpdateEvent && mIsAllDay ==1) {
            timePicker1.removeClickListener();
            time1.setText("");
            timePicker2.removeClickListener();
            time2.setText("");
        }
    }

    private void fillForm() {
        EditText title = findViewById(R.id.editTitle);
        EditText description = findViewById(R.id.editDesc);
        Switch isAllDay = findViewById(R.id.toggBtn);
        title.setText(mTitle);
        description.setText(mDescription);
        isAllDay.setChecked(true);

    }

    // When All-Day switch is turned on, the start & end time should not be visible.
    private void handleSwitchChange() {
        Switch onOffSwitch = findViewById(R.id.toggBtn);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AddEditActivity.this.time1.post(new Runnable() {
                        public void run() {
                            timePicker1.removeClickListener();
                            time1.setText("");
                        }
                    });

                    AddEditActivity.this.time2.post(new Runnable() {
                        public void run() {
                            timePicker2.removeClickListener();
                            time2.setText("");
                        }
                    });
                    mIsAllDay = 1;
                } else {
                    mIsAllDay = 0;
                    setDateTime();
                }
            }

        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.done) {
            handleSaveClick();
        }
        if (item.getItemId() == R.id.delete) {
            mRequestCode = MainActivity.DELETE_EVENT_ACTIVITY_REQUEST_CODE;
            handleSaveClick();
        }
        return true;
    }

    private void handleSaveClick() {
        int res = isFormValid();
        if (res == ERR_START_AFTER_END) {
            Toast.makeText(this, getResources().getString(R.string.end_before_start),
                    Toast.LENGTH_LONG).show();
        } else if (res == ERR_TIME_MORE_24) {
            Toast.makeText(this, getResources().getString(R.string.time_more_than_24),
                    Toast.LENGTH_LONG).show();
        } else {

            EditText title = findViewById(R.id.editTitle);
            EditText description = findViewById(R.id.editDesc);
            Switch isAllDay = findViewById(R.id.toggBtn);
            int allDay = 0;
            if(isAllDay.isChecked()) allDay = 1;
            int allDayNumber = datePicker2.day - datePicker1.day;


                Intent replyIntent = new Intent();

                if (res!= NO_ERR) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    // Put the new event in the extras for the reply Intent.
                    replyIntent.putExtra(EXTRA_REPLY_TITLE, mTitle);
                    if (mExtras != null && mExtras.containsKey(EXTRA_DATA_ID)) {
                        int id = mExtras.getInt(EXTRA_DATA_ID, -1);
                        if (id != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_RESPONSE_TYPE, mRequestCode);
                            replyIntent.putExtra(EXTRA_REPLY_TITLE, title.getText().toString());
                            replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, description.getText().toString());
                            replyIntent.putExtra(EXTRA_REPLY_IS_ALL_DAY, allDay);
                            replyIntent.putExtra(EXTRA_REPLY_IS_ALL_DAY_NUMBER, allDayNumber);
                            replyIntent.putExtra(EXTRA_REPLY_START_DAY, datePicker1.day);
                            replyIntent.putExtra(EXTRA_REPLY_END_DAY, datePicker2.day);
                            replyIntent.putExtra(EXTRA_REPLY_START_MONTH, datePicker1.month);
                            replyIntent.putExtra(EXTRA_REPLY_END_MONTH, datePicker2.month);
                            replyIntent.putExtra(EXTRA_REPLY_START_YEAR, datePicker1.year);
                            replyIntent.putExtra(EXTRA_REPLY_END_YEAR, datePicker2.year);
                            replyIntent.putExtra(EXTRA_REPLY_START_HOUR, timePicker1.hour);
                            replyIntent.putExtra(EXTRA_REPLY_END_HOUR, timePicker2.hour);
                            replyIntent.putExtra(EXTRA_REPLY_START_MINUTE, timePicker1.minute);
                            replyIntent.putExtra(EXTRA_REPLY_END_MINUTE, timePicker2.minute);
                        }
                    }
                    // Set the result status to indicate success.
                    setResult(RESULT_OK, replyIntent);

                }

                finish();
        }
    }
    //Ensure user input is valid
    private int isFormValid() {
        Calendar start = new GregorianCalendar(datePicker1.year, datePicker1.month, datePicker1.day, timePicker1.hour, timePicker1.minute);
        Calendar end = new GregorianCalendar(datePicker2.year, datePicker2.month, datePicker2.day, timePicker2.hour, timePicker2.minute);
        if (end.before(start))
            return ERR_START_AFTER_END;
        Switch onOffSwitch = (Switch) findViewById(R.id.toggBtn);
        if (!onOffSwitch.isChecked()) {
            long seconds = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000;
            if (seconds > SECONDS_IN_A_DAY)
                return ERR_START_AFTER_END;
        }
        return NO_ERR;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete = menu.findItem(R.id.delete);
        if (!mIsUpdateEvent)
            delete.setVisible(false);
        return true;
    }
}
