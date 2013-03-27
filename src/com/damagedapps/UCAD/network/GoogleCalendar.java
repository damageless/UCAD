package com.damagedapps.UCAD.network;


import com.damagedapps.UCAD.helper.CalendarParser;
import com.damagedapps.UCAD.helper.LogHelper;
import com.damagedapps.UCAD.model.GoogleCalendarEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

public class GoogleCalendar extends AsyncHttpResponseHandler {
    private static String URL = "https://www.google.com/calendar/feeds/0q35j58uekk7g0574cckc82kq4%40group.calendar.google.com/public/basic";
    private String TAG = LogHelper.makeTag(GoogleCalendar.class);
    private Listener mListener;
    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public GoogleCalendar() {}

    public interface Listener {
        void calendarResult(ArrayList<GoogleCalendarEvent> googleCalendarEvents);
    }

    public void getCalendar() {
        mClient.get(URL, null, this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onSuccess(String s) {
        mListener.calendarResult(parseResponse(s));
    }

    private ArrayList<GoogleCalendarEvent> parseResponse(String response) {
        CalendarParser parser = new CalendarParser(response);
        return parser.getCalendarEvents();
    }
}
