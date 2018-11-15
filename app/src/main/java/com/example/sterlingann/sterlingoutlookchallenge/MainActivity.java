package com.example.sterlingann.sterlingoutlookchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private EventViewModel mEventViewModel;
    List<AdapterContainer> mItemsToRV;
    RecyclerView mRecyclerView;
    int mDay, mMonth, mYear, mDayOfWeek;
    Calendar mCalendar;
    Context mContext;
    Event mReturnedEvent;
    private CalendarDateAdapter mCalendarDateAdapter;
    private GridView mGridView;

    public static final int NEW_EVENT_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_EVENT_ACTIVITY_REQUEST_CODE = 2;
    public static final int DELETE_EVENT_ACTIVITY_REQUEST_CODE = 3;
    public static final String EXTRA_DATA_UPDATE_EVENT = "extra_event_to_be_updated";
    public static final String EXTRA_DATA_NEW_EVENT = "extra_event_to_be_created";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_TITLE = "extra_data_title";
    public static final String EXTRA_DATA_DESCRIPTION = "extra_data_description";
    public static final String EXTRA_DATA_IS_ALL_DAY = "extra_data_is_all_day";
    public static final String EXTRA_DATA_IS_ALL_DAY_NUMBER = "extra_data_is_all_day_number";
    public static final String EXTRA_DATA_START_DAY = "extra_data_start_day";
    public static final String EXTRA_DATA_END_DAY = "extra_data_end_day";
    public static final String EXTRA_DATA_START_MONTH = "extra_data_start_month";
    public static final String EXTRA_DATA_END_MONTH = "extra_data_end_month";
    public static final String EXTRA_DATA_START_YEAR = "extra_data_start_year";
    public static final String EXTRA_DATA_END_YEAR = "extra_data_end_year";
    public static final String EXTRA_DATA_START_HOUR = "extra_data_start_hour";
    public static final String EXTRA_DATA_END_HOUR = "extra_data_end_hour";
    public static final String EXTRA_DATA_START_MINUTE = "extra_data_start_minute";
    public static final String EXTRA_DATA_END_MINUTE = "extra_data_end_minute";
    public static final String ACTION_DATE_CLICK = "action_date_click";
    public static final String CAL_POSITION = "calendar_position";
    List<CalendarData> mCalendarDataList = new ArrayList<CalendarData>();
    List<AdapterContainer> mContainerList = new ArrayList<AdapterContainer>();
    LinearLayoutManager mLinearLayoutManager;
    private int mGridIndex;
    private AgendaAdapter mAgendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mContext = this;
        mCalendar = Calendar.getInstance();
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mMonth = mCalendar.get(Calendar.MONTH);
        mYear = mCalendar.get(Calendar.YEAR);
        mDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA_NEW_EVENT, false);
                intent.setClass(mContext, AddEditActivity.class);
                startActivity(intent);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);

        // Set up the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);
        mAgendaAdapter = new AgendaAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAgendaAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set up the EventViewModel.
        mEventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // Get all the events from the database
        // and associate them to the agenda adapter.
        mEventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {

                HashMap<String, List<Event>> masterMap = null;
                if (events != null) {
                    masterMap = makeEventMap(events);
                }
                mItemsToRV = createContainerList(masterMap);
                mAgendaAdapter.setContainers(mItemsToRV);
                // When the app starts we want to Scroll to the Agenda for Today.
                String str = DateTimeUtils.formattedDate(mContext, mDayOfWeek, mMonth, mDay, mYear);
                int index = 1;
                for (int i = 0; i < mItemsToRV.size(); i++) {
                    if (mItemsToRV.get(i).isHeader) {
                        if (mItemsToRV.get(i).header.equals(str)) {
                            index = i;
                            break;
                        }
                    }
                }
                mRecyclerView.getLayoutManager().scrollToPosition(index);
                mAgendaAdapter.setOnItemClickListener(new AgendaAdapter.ClickListener() {
                                                          @Override
                                                          public void onItemClick(View v, int position) {
                                                              try {
                                                                  Event event = mAgendaAdapter.getEventAtPosition(position);
                                                                  launchAddEditActivity(event);
                                                              } catch (NullPointerException npe) {

                                                              }
                                                          }
                                                      }
                );
                setGridView();
            }
        });
    }

    private HashMap<String, List<Event>> makeEventMap(List<Event> incomingEvents) {
        HashMap<String, List<Event>> masterMap = new HashMap<String, List<Event>>();
        String key = null;

        for (Event event : incomingEvents) {
            if (event.getmIsAllDay() == 0) {
                key = DateTimeUtils.formattedDate(this, event.getmIsAllDayNumber(), event.getmStartMonth(), event.getmStartDay(), event.getmStartYear());
                if (!masterMap.containsKey(key)) {
                    List<Event> emptyList = new ArrayList<Event>();
                    emptyList.add(event);
                    masterMap.put(key, emptyList);
                }
                List<Event> thisEventList = masterMap.get(key);
                thisEventList.add(event);
                masterMap.put(key, thisEventList);
            } else {
                //separate into one event for each day for an extended "all day" event
                Calendar cal = new GregorianCalendar(event.getmStartYear(), event.getmStartMonth(), event.getmStartDay());
                Calendar orig = (Calendar) cal.clone();
                int daysLeft = event.getmEndDay() - event.getmStartDay();
                for (int i = 0; i <= daysLeft; i++) {
                    Event newEvent = new Event(event.getId(), event.getmTitle(), event.getmDescription(), event.getmIsAllDay(), event.getmIsAllDayNumber(), cal.get(Calendar.DAY_OF_MONTH), event.getmEndDay(), cal.get(Calendar.MONTH), event.getmEndMonth(), cal.get(Calendar.YEAR), event.getmEndYear(), 8, event.getmEndHour(), 1, event.getmEndMinute());
                    newEvent.setmStartDay(orig.get(Calendar.DAY_OF_MONTH));
                    newEvent.setmStartMonth(orig.get(Calendar.MONTH));
                    event.setmStartYear(orig.get(Calendar.YEAR));
                    key = DateTimeUtils.formattedDate(this, cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));

                    if (!masterMap.containsKey(key)) {
                        List<Event> emptyList = new ArrayList<Event>();
                        emptyList.add(event);
                        masterMap.put(key, emptyList);
                    }
                }
            }
        }
        return masterMap;
    }

   //populate list for agenda view
    private List<AdapterContainer> createContainerList(HashMap<String, List<Event>> masterMap) {
        List<AdapterContainer> containerListLocal = new ArrayList<AdapterContainer>();
        List<CalendarData> calendarDataListLocal = new ArrayList<CalendarData>();

       //time span of prevoius year to next year
        calendarDataListLocal = DateTimeUtils.getCalendarTwoYears(this);

        for (CalendarData date : calendarDataListLocal) {
            DayEventsData agendaRow = new DayEventsData();

            // If the map contains they key, we know there are events for this day
            if (masterMap.containsKey(date.formattedDate)) {
                agendaRow.isRegularEvent = true;
                agendaRow.eventList = masterMap.get(date.formattedDate);
                // Sort the events in a particular day
                Collections.sort(agendaRow.eventList, new EventComparator());
            } else {
                agendaRow.isNoEvent = true;
            }

            // Add a header container for every day in the calendar list
            AdapterContainer headerContainer = new AdapterContainer();
            headerContainer.isHeader = true;
            headerContainer.header = date.formattedDate;
            containerListLocal.add(headerContainer);

            // Container for Day with no events
            if (agendaRow.isRegularEvent) {
                for (Event event : agendaRow.eventList) {
                    AdapterContainer eventContainer = new AdapterContainer();
                    eventContainer.isRegularEvent = true;
                    eventContainer.event = event;
                    containerListLocal.add(eventContainer);
                }

            } else if (agendaRow.isNoEvent) {
                AdapterContainer noEventContainer = new AdapterContainer();
                noEventContainer.isNoEvent = true;
                containerListLocal.add(noEventContainer);
            }
        }
        mCalendarDataList = calendarDataListLocal;
        mContainerList = containerListLocal;
        return containerListLocal;
    }

    /**
     * When the user adds or edits an event in the AddEditActivity,
     * that activity returns the result to this activity.
     * Save, update or delete event in database when appropriate
     *
     * @param requestCode ID for the request
     * @param resultCode  indicates success or failure
     * @param data        The Intent sent back from the AddEditActivity,
     *                    which includes the event that the user entered
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int responseCode = data.getIntExtra(AddEditActivity.EXTRA_REPLY_RESPONSE_TYPE, 0);
            String title = data.getStringExtra(AddEditActivity.EXTRA_REPLY_TITLE);
            String description = data.getStringExtra(AddEditActivity.EXTRA_REPLY_DESCRIPTION);
            int isAllDay = data.getIntExtra(AddEditActivity.EXTRA_REPLY_IS_ALL_DAY, 0);
            int isAllDayNumber = data.getIntExtra(AddEditActivity.EXTRA_REPLY_IS_ALL_DAY_NUMBER, 0);
            int startDay = data.getIntExtra(AddEditActivity.EXTRA_REPLY_START_DAY, 1);
            int endDay = data.getIntExtra(AddEditActivity.EXTRA_REPLY_END_DAY, 1);
            int startMonth = data.getIntExtra(AddEditActivity.EXTRA_REPLY_START_MONTH, 1);
            int endMonth = data.getIntExtra(AddEditActivity.EXTRA_REPLY_END_MONTH, 1);
            int startYear = data.getIntExtra(AddEditActivity.EXTRA_REPLY_START_YEAR, 2018);
            int endYear = data.getIntExtra(AddEditActivity.EXTRA_REPLY_END_YEAR, 2018);
            int startHour = data.getIntExtra(AddEditActivity.EXTRA_REPLY_START_HOUR, 8);
            int endHour = data.getIntExtra(AddEditActivity.EXTRA_REPLY_END_HOUR, 9);
            int startMinute = data.getIntExtra(AddEditActivity.EXTRA_REPLY_START_MINUTE, 2);
            int endMinute = data.getIntExtra(AddEditActivity.EXTRA_REPLY_END_MINUTE, 30);

            mReturnedEvent = new Event(title, description, isAllDay, isAllDayNumber, startDay, endDay, startMonth, endMonth, startYear, endYear, startHour, endHour, startMinute, endMinute);
            if (responseCode == NEW_EVENT_ACTIVITY_REQUEST_CODE) {
                mEventViewModel.insert(mReturnedEvent);

            } else if (responseCode == UPDATE_EVENT_ACTIVITY_REQUEST_CODE) {
                mEventViewModel.update(mReturnedEvent);
            } else if (responseCode == DELETE_EVENT_ACTIVITY_REQUEST_CODE) {
                mEventViewModel.deleteEvent(mReturnedEvent);
            }
            mAgendaAdapter.notifyDataSetChanged();
        }
    }

    public void launchAddEditActivity(Event event) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(EXTRA_DATA_NEW_EVENT, true);
        intent.putExtra(EXTRA_DATA_ID, event.getId());
        intent.putExtra(EXTRA_DATA_TITLE, event.getmTitle());
        intent.putExtra(EXTRA_DATA_DESCRIPTION, event.getmDescription());
        intent.putExtra(EXTRA_DATA_IS_ALL_DAY, event.getmIsAllDay());
        intent.putExtra(EXTRA_DATA_IS_ALL_DAY_NUMBER, event.getmIsAllDayNumber());
        intent.putExtra(EXTRA_DATA_START_DAY, event.getmStartDay());
        intent.putExtra(EXTRA_DATA_END_DAY, event.getmEndDay());
        intent.putExtra(EXTRA_DATA_START_MONTH, event.getmStartMonth());
        intent.putExtra(EXTRA_DATA_END_MONTH, event.getmEndMonth());
        intent.putExtra(EXTRA_DATA_START_YEAR, event.getmEndYear());
        intent.putExtra(EXTRA_DATA_END_YEAR, event.getmEndYear());
        intent.putExtra(EXTRA_DATA_START_HOUR, event.getmStartHour());
        intent.putExtra(EXTRA_DATA_END_HOUR, event.getmEndHour());
        intent.putExtra(EXTRA_DATA_START_MINUTE, event.getmStartMinute());
        intent.putExtra(EXTRA_DATA_END_MINUTE, event.getmEndMinute());
        startActivityForResult(intent, UPDATE_EVENT_ACTIVITY_REQUEST_CODE);
    }

    private void setGridView() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        mCalendarDateAdapter = new CalendarDateAdapter(this, mCalendarDataList);

        setGridAdapter(mCalendarDateAdapter);

        getGridView().setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstVisiblePosition;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisiblePosition = getGridView().getFirstVisiblePosition();
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final GridView lw = getGridView();

                if (scrollState == SCROLL_STATE_IDLE)
                    return;
                // Expand calendar grid height while scrolling

                if (view.getId() == lw.getId()) {
                    getGridView().setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, (int) mContext.getResources().getDimension(R.dimen.long_cal_height)));
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy ) {
                super.onScrolled(recyclerView, dx, dy);
        //highlight corresponding day in calendar view
                runOnUiThread(new Runnable() {
                    int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();

                    public void run() {
                        if (mContainerList.size() > 0) {
                            while (!mContainerList.get(firstVisiblePosition).isHeader)
                                firstVisiblePosition = firstVisiblePosition - 1;

                            /* Calendar Date starts from last sunday before Same date one year ago */
                            Calendar startDate = Calendar.getInstance(TimeZone.getDefault());
                            startDate.add(Calendar.YEAR, -1);
                            while (startDate.get(Calendar.DAY_OF_WEEK) != 1)
                                startDate.add(Calendar.DATE, -1);

                            // Calendar for the first day who's event(s) are visible in the agenda view.
                            Calendar calendar = DateTimeUtils.parseDate(mContext, mContainerList.get(firstVisiblePosition).header);
                            int gridIndex = 1;
                            // Find the correct position which needs to be highlighted in the Calendar view.
                            gridIndex = DateTimeUtils.getDurationInDays(startDate, calendar);
                            mCalendarDataList.get(gridIndex).isCurrent = true;

                            // Reset the old highlighted date in the Calendar view
                            if (mGridIndex != gridIndex)
                                mCalendarDataList.get(mGridIndex).isCurrent = false;
                            mGridIndex = gridIndex;
                            // Update the Calendar view
                            mCalendarDateAdapter.notifyDataSetChanged();
                            // Scroll to the particular position in the Calendar view
                            getGridView().post(new Runnable() {
                                @Override
                                public void run() {
                                    getGridView().setSelection(mGridIndex);
                                }
                            });
                        }
                    }
                });

                // When the app starts we want to Scroll to the Agenda for Today.
                String str = DateTimeUtils.formattedDate(mContext, mDayOfWeek, mMonth, mDay, mYear);
                int index = 1;

                for(int i = 0; i < mContainerList.size(); i++) {
                    if (mContainerList.get(i).isHeader) {
                        if (mContainerList.get(i).header.equals(str)) {
                            index = i;
                            break;
                        }
                    }
                }

                mRecyclerView.scrollToPosition(index);
                mAgendaAdapter.notifyDataSetChanged();

                Calendar startDate = Calendar.getInstance(TimeZone.getDefault());
                startDate.add(Calendar.YEAR, -1);
                while (startDate.get(Calendar.DAY_OF_WEEK) != 1)
                    startDate.add(Calendar.DATE, -1);
                int gridIndex = DateTimeUtils.getDurationInDays(startDate, Calendar.getInstance());
                getGridView().setSelection(gridIndex);
                mGridIndex = gridIndex;
            }
        });
    }

    protected GridView getGridView() {
        if (mGridView == null) {
            mGridView = findViewById(R.id.gridView1);
        }
        return mGridView;
    }
    protected void setGridAdapter(ListAdapter adapter) {
        getGridView().setAdapter(adapter);
    }
}
