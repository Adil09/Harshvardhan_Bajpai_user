package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

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

public class ListProgram extends AppCompatActivity {
    ArrayList<GetSetCalEvent> getSetCalEvents;
    CompactCalendarView compactCalendarView;
    TextView upcomingdata, upcomingtxt;
    RecyclerView holidays_recycler;
    String l, uid;
    TextView upcomingevents, events1,text;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    TextView events_btn;
    TextView previous_month, next_month;
    private SimpleDateFormat dateFormatMonthNum = new SimpleDateFormat("MM", Locale.getDefault());
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Date_holiday = new ArrayList<>();
    ImageView holi_arrow;
    private ProgressDialog progressDialog;
    ListView listView, listView1;
    private Date currentDate = new Date();
    String monn="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_program);
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        l = ss.getString("user_id", "N/A");


        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        uid = s.getString("mla_id", "N/A");

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
        text=findViewById(R.id.text);
        upcomingevents = findViewById(R.id.upcoming);
        previous_month = findViewById(R.id.previous_month);
        next_month = findViewById(R.id.next_month);
        listView = (ListView) findViewById(R.id.calendar_event_list);
        listView1 = (ListView) findViewById(R.id.calendar_event_list1);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                // Getting listview click value into String variable.


            }
        });
        events_btn.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        eventsHelper(String.valueOf(getDate(System.currentTimeMillis())));
// Calendar CLick listener when hte user click on the date
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Log.d("checkdatee", String.valueOf(dateClicked));
//                events1.setText("");
//                List<Event> events = compactCalendarView.getEvents(dateClicked);
////                Log.d("Events_events", "Day was clicked: " + dateClicked + " with Events " + events);
//                if (events != null) {
//                    for(int i =0 ; i<events.size();i++) {
////                        Log.i("check_events_dt", events.toString());
//                        // Toast.makeText(getActivity(), Events + "", Toast.LENGTH_LONG).show();
//                        String data = events.get(i).getData().toString();
//                        events1.append(i+1 +" : " + data + "\n" );
//                    }
//                    events_btn.setText(dateFormatForMonth.format(dateClicked));
//                }
//                else
//                {
//                    events1.setText("");
//                }
                listView.setAdapter(null);
                listView1.setAdapter(null);
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);


                if (bookingsFromMap != null) {

                    for (int i = 0; i < bookingsFromMap.size(); i++) {
                        EventListAdapter adapter = new EventListAdapter(getApplicationContext(), bookingsFromMap);
                        listView.setAdapter(adapter);
                    }
                    events_btn.setText(dateFormatForMonth.format(dateClicked));
                } else {

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                eventsHelper(dateFormatMonthNum.format(firstDayOfNewMonth));
                compactCalendarView.removeAllEvents();
                listView.setAdapter(null);
                listView1.setAdapter(null);
                events_btn.setText(dateFormatForMonth.format(firstDayOfNewMonth));

                Log.d("checkdatee", String.valueOf(firstDayOfNewMonth));
                events1.setText("");


//                List<Event> bookingsFromMap = compactCalendarView.getEventsForMonth(firstDayOfNewMonth.getTime());
//
//
//                if (bookingsFromMap != null) {
//
//                    for (int i = 0; i < bookingsFromMap.size(); i++) {
//
//                        EventListAdapter1 adapter = new EventListAdapter1(getApplicationContext(), bookingsFromMap);
//                        listView1.setAdapter(adapter);
//                    }
//                    /// events_btn.setText(dateFormatForMonth.format(dateClicked));
//                } else {
//
//                }

//                List<Event> bookingsFromMap = compactCalendarView.getEventsForMonth(firstDayOfNewMonth.getTime());
//
//
//                if (bookingsFromMap != null) {
//
//                    for (int i = 0; i < bookingsFromMap.size(); i++) {
//
//                        EventListAdapter1 adapter = new EventListAdapter1(getApplicationContext(), bookingsFromMap);
//                        listView1.setAdapter(adapter);
//                    }
//                    /// events_btn.setText(dateFormatForMonth.format(dateClicked));
//                } else {
//listView1.setAdapter(null);
//                }
            }
        });
        call_method(l);
    }



    public class EventListAdapter extends ArrayAdapter<Event> {
        Context context;

        public EventListAdapter(Context context, List<Event> feeds) {
            super(context, 0, feeds);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Event feed = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_feed_item_row, parent, false);
            }

            ImageView eventName = (ImageView) convertView.findViewById(R.id.event_list_dataText);
            TextView eventDay = (TextView) convertView.findViewById(R.id.event_list_dayTextView);
            TextView eventMonth = (TextView) convertView.findViewById(R.id.event_list_monthTextView);

            SimpleDateFormat simple_month = new SimpleDateFormat("MMM", Locale.US);
            SimpleDateFormat simple_day = new SimpleDateFormat("dd", Locale.US);
            // eventName.setText((String) feed.getData());
            Picasso.with(getApplicationContext()).load("http://mla-admin.sitsald.co.in/" + feed.getData()).into(eventName);

            eventMonth.setText(simple_month.format(feed.getTimeInMillis()));
            eventDay.setText(simple_day.format(feed.getTimeInMillis()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String TempListViewClickedValue = "http://mla-admin.sitsald.co.in/" + feed.getData();

                    Intent intent = new Intent(getApplicationContext(), ViewFullImage.class);

                    // Sending value to another activity using intent.
                    intent.putExtra("image", TempListViewClickedValue);

                    startActivity(intent);
                }
            });


            return convertView;
        }
    }

    public class EventListAdapter1 extends ArrayAdapter<Event> {
        Context context;

        public EventListAdapter1(Context context, List<Event> feeds) {
            super(context, 0, feeds);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Event feed = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_feed_item_row, parent, false);
            }

            ImageView eventName = (ImageView) convertView.findViewById(R.id.event_list_dataText);
            TextView eventDay = (TextView) convertView.findViewById(R.id.event_list_dayTextView);
            TextView eventMonth = (TextView) convertView.findViewById(R.id.event_list_monthTextView);

            SimpleDateFormat simple_month = new SimpleDateFormat("MMM", Locale.US);
            SimpleDateFormat simple_day = new SimpleDateFormat("dd", Locale.US);
            // eventName.setText((String) feed.getData());
            Picasso.with(getApplicationContext()).load("http://mla-admin.sitsald.co.in/" + feed.getData()).into(eventName);

            eventMonth.setText(simple_month.format(feed.getTimeInMillis()));
            eventDay.setText(simple_day.format(feed.getTimeInMillis()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String TempListViewClickedValue = "http://mla-admin.sitsald.co.in/" + feed.getData();

                    Intent intent = new Intent(getApplicationContext(), ViewFullImage.class);

                    // Sending value to another activity using intent.
                    intent.putExtra("image", TempListViewClickedValue);

                    startActivity(intent);
                }
            });
            Date date1 = new Date();
            String modifiedDate= new SimpleDateFormat("dd").format(date1);


            return convertView;
        }
    }



    private void call_method(final String lan) {
        if(lan.equals("English")){

            text.setText("PROGRAM LIST ");

            // send_eng();
        }else if(lan.equals("Hindi")) {

            text.setText("प्रोग्राम लिस्ट ");


        }
    }
    private void getCalEvents(final String Month) {

        String dateno = "";
        if(Month.equals("01")){
            dateno="1";
        }else if(Month.equals("02")){
            dateno="2";
        }if(Month.equals("03")){
            dateno="3";
        }else if(Month.equals("04")){
            dateno="4";
        }
        if(Month.equals("05")){
            dateno="5";
        }else if(Month.equals("06")){
            dateno="6";
        }if(Month.equals("07")){
            dateno="7";
        }else if(Month.equals("08")){
            dateno="8";
        }if(Month.equals("09")){
            dateno="9";
        }else if(Month.equals("10")){
            dateno="10";
        }if(Month.equals("11")){
            dateno="11";
        }else if(Month.equals("12")){
            dateno="12";
        }
        Date_holiday.clear();
        Names.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_program_list_by_month/"+dateno+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalendarResponse", "onResponse: " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject=jsonObject1.getJSONObject("message");

                        JSONArray jsonArray = jsonObject.getJSONArray("program_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                            JSONObject program_lists = jsonObject11.getJSONObject("ProgramList");


                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                            String ds2 = sdf2.format(sdf1.parse(program_lists.optString("p_date")));
                            Date_holiday.add(ds2);
                            Names.add(program_lists.optString("p_image"));



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < Date_holiday.size(); i++) {
                    Log.d("GetSetCalEventList", "Date : " + Date_holiday.get(i) + "Time : " + Names.get(i));
                    Event ev = new Event(Color.parseColor("#5ca3ff"), convertDateToTimestampnewevent(Date_holiday.get(i)), Names.get(i));
                    compactCalendarView.addEvent(ev);
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate = formatter.format(date);




                }
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = formatter.format(date);
                Log.d("GetSeNo", "Date : " + monn+"  "+currentDate);
if(Integer.parseInt(Month)==Integer.parseInt(monn)){
    listView1.setVisibility(View.VISIBLE);
    List<Event> bookingsFromMap = compactCalendarView.getEvents(date.getTime());

    if (bookingsFromMap != null) {

        for (int i = 0; i < bookingsFromMap.size(); i++) {

            EventListAdapter1 adapter = new EventListAdapter1(getApplicationContext(), bookingsFromMap);
            listView1.setAdapter(adapter);
        }
        /// events_btn.setText(dateFormatForMonth.format(dateClicked));
    }
}else {
    listView1.setAdapter(null);
    listView1.setVisibility(View.GONE);
}



//                if(eventsupcoming.size()>0)
//                {
//                    upcomingtxt.setVisibility(View.VISIBLE);
//                    for(int i =0 ; i<eventsupcoming.size();i++) {
//                        String data = eventsupcoming.get(i).getData().toString();
//                        upcomingevents.append(i+1 +" --> " + data + "\n");
//                    }
//                }else{
//                    upcomingtxt.setVisibility(View.GONE);
//                    upcomingevents.setText("");
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> values = new HashMap<>();
                values.put("student_id", "672");
                values.put("subdomain", "CPSPBH");
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
    private String getDate(long milliSeconds) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(milliSeconds);
        String date = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString();
        String[] Montth = date.split("-");
        return Montth[1];
    }
    @SuppressLint("StaticFieldLeak")
    void eventsHelper(final String Month) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ListProgram.this);
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
                getCalEvents(Month);
                monn=Month;
                return null;


            }
        }.execute();
    }
}