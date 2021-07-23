package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TashHoli extends AppCompatActivity {
    ArrayList<GetSetCalEvent> getSetCalEvents;
    CompactCalendarView compactCalendarView;
    TextView upcomingdata, upcomingtxt;
    RecyclerView holidays_recycler;
    String uid, sub;
    TextView upcomingevents, events1;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    TextView events_btn;
    TextView previous_month, next_month;
    private SimpleDateFormat dateFormatMonthNum = new SimpleDateFormat("MM", Locale.getDefault());
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Date_holiday = new ArrayList<>();
    ImageView holi_arrow;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_tash_hol);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sss = getSharedPreferences("Student_id", MODE_PRIVATE);
        uid = sss.getString("user_id", "N/A");
        sub = sss.getString("sub_domain", "N/A");
        holi_arrow = findViewById(R.id.holi_arrow);
        holi_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSetCalEvents = new ArrayList<>();
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        events1 = findViewById(R.id.event1);
        upcomingevents = findViewById(R.id.upcoming);
        previous_month = findViewById(R.id.previous_month);
        next_month = findViewById(R.id.next_month);
        events_btn = findViewById(R.id.events_btn);
        upcomingtxt = findViewById(R.id.upcomingtxt);

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setCurrentDayIndicatorStyle(CompactCalendarView.SMALL_INDICATOR);
        compactCalendarView.setEventIndicatorStyle(CompactCalendarView.NO_FILL_LARGE_INDICATOR);

        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.showNextMonth();

            }
        });
//Showing Previous Month
        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.showPreviousMonth();
            }
        });

        events_btn.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        eventsHelper(String.valueOf(getDate(System.currentTimeMillis())));
// Calendar CLick listener when hte user click on the date
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                events1.setText("");
                List<Event> events = compactCalendarView.getEvents(dateClicked);
//                Log.d("Events_events", "Day was clicked: " + dateClicked + " with Events " + events);
                if (events != null) {
                    for (int i = 0; i < events.size(); i++) {
//                        Log.i("check_events_dt", events.toString());
                        // Toast.makeText(getActivity(), Events + "", Toast.LENGTH_LONG).show();
                        String data = events.get(i).getData().toString();
                        events1.append(i + 1 + " : " + data + "\n");
                    }
                    events_btn.setText(dateFormatForMonth.format(dateClicked));
                } else {
                    events1.setText("");
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                eventsHelper(dateFormatMonthNum.format(firstDayOfNewMonth));
                events_btn.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                events1.setText("");
                List<Event> eventsupcoming = compactCalendarView.getEventsForMonth(firstDayOfNewMonth.getTime());
                Log.d("Events_events", "Day was clicked: " + firstDayOfNewMonth.getTime() + " with Events " + eventsupcoming);
                if (eventsupcoming.size() > 0) {
                    upcomingevents.setText("");
                    upcomingtxt.setVisibility(View.VISIBLE);
                    for (int i = 0; i < eventsupcoming.size(); i++) {
                        String data = eventsupcoming.get(i).getData().toString();
                        upcomingevents.append(i + 1 + " --> " + data + "\n");
                        upcomingevents.setTextColor(Color.parseColor("#123123"));
                    }
                } else {
                    upcomingtxt.setVisibility(View.GONE);
                    upcomingevents.setText("");
                }
            }
        });
    }
    private String getDate(long milliSeconds) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(milliSeconds);
        String date = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString();
        String[] Montth = date.split("-");
        return Montth[1];
    }
    private void getCalEvents() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalendarResponse", "onResponse: " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (!jsonObject1.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("holidays");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject11 = jsonArray.getJSONObject(i);


                            Date_holiday.add(jsonObject11.optString("h_date").trim());
                            Names.add(jsonObject11.optString("name"));
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < Date_holiday.size(); i++) {
                    Log.d("GetSetCalEventList", "Date : " + Date_holiday.get(i) + "Time : " + Names.get(i));
                    Event ev = new Event(Color.parseColor("#5ca3ff"), convertDateToTimestampnewevent(Date_holiday.get(i)), Names.get(i));
                    compactCalendarView.addEvent(ev);
                }
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = formatter.format(date);
                List<Event> eventsupcoming;

                eventsupcoming = compactCalendarView.getEvents(date.getTime());

                if (eventsupcoming.size() > 0) {
                    upcomingtxt.setVisibility(View.VISIBLE);
                    for (int i = 0; i < eventsupcoming.size(); i++) {
                        String data = eventsupcoming.get(i).getData().toString();
                        upcomingevents.append(i + 1 + " --> " + data + "\n");
                    }
                } else {
                    upcomingtxt.setVisibility(View.GONE);
                    upcomingevents.setText("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> values = new HashMap<>();

                return values;
            }
        };
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    public static long convertDateToTimestampnewevent(String start_date) {
        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            date = formatter.parse(start_date);
            Log.i("GetSetCalEventList", date.getTime() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timestamp = date.getTime();
        Log.i("GetSetCalEventList", timestamp + "");
        return timestamp;
    }

    @SuppressLint("StaticFieldLeak")
    void eventsHelper(final String Month) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(TashHoli.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIcon(R.drawable.ic_date_range_black_24dp);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            progressDialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("checkmonth",Month);
                getCalEvents();
                return null;


            }
        }.execute();
    }
}