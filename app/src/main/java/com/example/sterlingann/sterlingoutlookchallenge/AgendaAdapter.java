package com.example.sterlingann.sterlingoutlookchallenge;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Adapter for the RecyclerView that displays a list of containers (event, no event, header).
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.EventViewHolder> {

    private final LayoutInflater mInflater;
    private List<AdapterContainer> mContainers; // Cached copy of containers
    private static ClickListener clickListener;

    AgendaAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1 (no event)

        if(mContainers.get(position).isHeader) viewType = 0;
        else if(mContainers.get(position).isRegularEvent) viewType = 2;
        else viewType = 1;
        return viewType;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        //header layout
        if(viewType == 0) itemView = mInflater.inflate(R.layout.header_layout, null);
        // no event layout
        else if(viewType == 1) itemView = mInflater.inflate(R.layout.no_event_layout, null);
       //regular event layout
        else if(viewType ==2)  itemView = mInflater.inflate(R.layout.event_layout, null);

        else itemView= mInflater.inflate(R.layout.no_event_layout,null);

        return new EventViewHolder(itemView, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (mContainers != null) {
            AdapterContainer current = mContainers.get(position);
            //header
            if (current.isHeader) {
                holder.headerTitle.setText(current.header);
            }
            //no event
            else if (current.isRegularEvent) {

                holder.eventTitle.setText(current.event.getmTitle());
                if (current.event.getmIsAllDay() == 1) holder.eventTime.setText(R.string.allday);
                else if(current.event.getmIsAllDay()==0) {
                    String time = DateTimeUtils.formattedTime(current.event.getmStartHour(), current.event.getmEndMinute()).toString();
                    holder.eventTime.setText(time);
                }
            }

        }
    }

    /**
     * Associates a list of event containers (event, no event, header) with this adapter
     */
    void setContainers(List<AdapterContainer> containers) {
        mContainers = containers;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mContainers has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mContainers != null)
            return mContainers.size();
        else return 0;
    }

    /**
     * Gets the event at a given position.
     * This method is useful for identifying which event
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the event in the RecyclerView
     * @return The event at the given position
     */
    public Event getEventAtPosition(int position) {
        return mContainers.get(position).event;
    }


    //custom event holder class which accomodates three different types of layouts

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView headerTitle, eventTitle, eventTime;
        private EventViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 0) {
                 headerTitle = itemView.findViewById(R.id.date_title);
            } else if(viewType ==1){
                //do nothing
            } else { //regular event type
                 eventTitle = (TextView) itemView.findViewById(R.id.curr_title);
                 eventTime = (TextView) itemView.findViewById(R.id.curr_time);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(view, getAdapterPosition());
                    }
                });

            }
        }
    }

    void setOnItemClickListener(ClickListener clickListener) {
        AgendaAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}