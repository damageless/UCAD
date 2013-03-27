package com.damagedapps.UCAD.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.damagedapps.UCAD.R;
import com.damagedapps.UCAD.model.GoogleCalendarEvent;
import com.damagedapps.UCAD.network.GoogleCalendar;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements GoogleCalendar.Listener {
    private GoogleCalendar mGoogleCalendar;
    private ProgressBar mProgress;
    private ExpandableListView mListView;
    private UCADCalendarExpandableListAdapter mListAdapter;
    private ArrayList<GoogleCalendarEvent> mGoogleCalendarEvents = new ArrayList<GoogleCalendarEvent>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mGoogleCalendar = new GoogleCalendar();
        mListView = (ExpandableListView) getListView();
        mGoogleCalendar.setListener(this);
        mGoogleCalendar.getCalendar();
        mListAdapter = new UCADCalendarExpandableListAdapter();
        mListView.setAdapter(mListAdapter);

        // Expand all groups
        for(int i = 0; i < mListAdapter.getGroupCount(); i++)
            mListView.expandGroup(i);
    }

    @Override
    public void calendarResult(ArrayList<GoogleCalendarEvent> googleCalendarEvents) {
        mProgress.setVisibility(View.GONE);
        mGoogleCalendarEvents = googleCalendarEvents;
        mListAdapter.notifyDataSetChanged();
    }

    private class UCADCalendarExpandableListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mGoogleCalendarEvents.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public GoogleCalendarEvent getGroup(int i) {
            return mGoogleCalendarEvents.get(i);
        }

        @Override
        public Object getChild(int i, int i2) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i2) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            HeaderViewHolder headerViewHolder;
            if(convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_ucad_calendar_header, null);
                headerViewHolder = new HeaderViewHolder();
                headerViewHolder.calendarHeaderNameTextView = (TextView) convertView.findViewById(R.id.header_name_textview);
                convertView.setTag(headerViewHolder);
            } else
                headerViewHolder = (HeaderViewHolder) convertView.getTag();
            headerViewHolder.calendarHeaderNameTextView.setText(mGoogleCalendarEvents.get(i).getTitle() +  " @ " +
                                                                mGoogleCalendarEvents.get(i).getLocation());

            return convertView;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View convertView, ViewGroup viewGroup) {
            ChildViewHolder childViewHolder;
            if(convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_ucad_calendar, null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.calendarDescriptionTextView = (TextView) convertView.findViewById(R.id.ucad_calendar_description_textview);
                childViewHolder.calendarEventDateTextView = (TextView) convertView.findViewById(R.id.ucad_calendar_date_textview);
                childViewHolder.calendarEventLocationTextView = (TextView) convertView.findViewById(R.id.ucad_calendar_location_textview);
                childViewHolder.calendarEventNameTextView = (TextView) convertView.findViewById(R.id.ucad_calendar_name_textview);
                convertView.setTag(childViewHolder);
            } else
                childViewHolder = (ChildViewHolder) convertView.getTag();
            GoogleCalendarEvent gce = mGoogleCalendarEvents.get(i);
            childViewHolder.calendarEventNameTextView.setText(gce.getTitle());
            childViewHolder.calendarEventDateTextView.setText(gce.getDate());
            childViewHolder.calendarEventLocationTextView.setText(gce.getLocation());
//            childViewHolder.calendarDescriptionTextView.setText(gce.getDescription());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return false;
        }

        private class ChildViewHolder {
            TextView calendarEventNameTextView;
            TextView calendarEventDateTextView;
            TextView calendarEventLocationTextView;
            TextView calendarDescriptionTextView;
        }

        private class HeaderViewHolder {
            TextView calendarHeaderNameTextView;
        }
    }
}
