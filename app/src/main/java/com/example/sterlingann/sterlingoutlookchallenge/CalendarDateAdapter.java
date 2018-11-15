package com.example.sterlingann.sterlingoutlookchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class CalendarDateAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CalendarData> mContainerList;

    CalendarDateAdapter(Context context, List<CalendarData> containerList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mContainerList = containerList;
    }

    @Override
    public int getCount() {
        return mContainerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mContainerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int currPosition = position;
        ViewHolder mHolder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.date_cell, null);
            mHolder.date = (TextView) convertView.findViewById(R.id.cell);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        CalendarData data = mContainerList.get(position);
        int day = data.calendar.get(Calendar.DAY_OF_MONTH);

        if (day != 1 || data.isCurrent)
            if (day < 10)
                mHolder.date.setText("  " + day + "  ");
            else
                mHolder.date.setText(" " + day + " ");
        else {
            // Special Handling for 1st Day of the month
            final SpannableString text = new SpannableString("" + day + " " + DateTimeUtils.convertMonthIntToStringShort(mContext, data.calendar.get(Calendar.MONTH)));
            text.setSpan(new RelativeSizeSpan(0.5f), 0, 0,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new RelativeSizeSpan(0.5f), 2, 5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.date.setText(text);
        }

        // Handle the highlighted date
        if (data.isCurrent) {
            mHolder.date.setBackgroundResource(R.drawable.shape);
            mHolder.date.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            mHolder.date.setBackgroundResource(R.drawable.shape_white);
            mHolder.date.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        }

        mHolder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.ACTION_DATE_CLICK);
                intent.putExtra(MainActivity.CAL_POSITION, currPosition);

            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView date;
    }
}