package com.harshvardhanbajpai;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.impulsive.zoomimageview.ZoomImageView;
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
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Holidays extends AppCompatActivity {
    ArrayList<GetSetCalEvent> getSetCalEvents;
    CompactCalendarView compactCalendarView;
    TextView upcomingdata , upcomingtxt;
    RecyclerView holidays_recycler;
    String uid,sub;
   TextView upcomingevents , events1 ;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("dd- MMM - yyyy", Locale.getDefault());
TextView events_btn;
    TextView previous_month,next_month,text;
    private HorizontalCalendar horizontalCalendar;
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Date_holiday = new ArrayList<>();
    ArrayList<GetSetProgram>p_list=new ArrayList<>();
    RecyclerView p_recycler;
LinearLayout toolbar_title_back;
  private ProgressDialog progressDialog;
  TextView next_pname;
  String l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_program);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        l = ss.getString("user_id", "N/A");


        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        uid = s.getString("mla_id", "N/A");
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        getSetCalEvents = new ArrayList<>();
        text=findViewById(R.id.text);

        p_recycler=findViewById(R.id.p_recycler);
        next_pname=findViewById(R.id.next_pname);
        call_method(l);
        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String formattedDate = df.format(c);
       eventsHelper(formattedDate);

        /* start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        /* end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate)

                .build();

        //Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Calendar cal = horizontalCalendar.getDateAt(position);


                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                String formatted = format1.format(cal.getTime());
                eventsHelper(formatted);
                Log.d("SelectedDate", formatted);
                Log.d("onDateSelected", date.toString() + " - Position = " + position);
            }

        });



    }
    private void call_method(final String lan) {
        if(lan.equals("English")){

            text.setText("PROGRAM LIST ");

            // send_eng();
        }else if(lan.equals("Hindi")) {

            text.setText("प्रोग्राम लिस्ट ");


        }
    }


    private void getCalEvents(final String date) {
        Log.d("checkdate",date);
        p_list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_program_list/"+date+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("CalendarResponse", "onResponse: " + response);
                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("program_list");
                    if(l.equals("English")){


                        next_pname.setText("No program on "+date+" ");
                        // send_eng();
                    }else if(l.equals("Hindi")) {

                        next_pname.setText(date+" "+"को कोई प्रोग्राम नही है ");


                    }
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("ProgramList");

                            p_list.add(new GetSetProgram(packagedata.getString("id"), packagedata.getString("p_image")));
                        }

                    if (p_list.size() > 0) {
                        p_recycler.setVisibility(View.VISIBLE);
                        next_pname.setVisibility(View.GONE);
                        p_recycler.setLayoutManager(new LinearLayoutManager(Holidays.this, LinearLayoutManager.VERTICAL, false));
                        p_recycler.setAdapter(new ProgramListRecycler(Holidays.this, R.layout.program_list_recycler, p_list));

                    }else {
p_recycler.setVisibility(View.GONE);
                        next_pname.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    public class ProgramListRecycler extends RecyclerView.Adapter<ProgramListRecycler.ViewHolder>{

        Context applicationContext;
        int retailer_weekly_sku_item;
        ArrayList<GetSetProgram> retailer_weekly_sku_model;

        public ProgramListRecycler(Context applicationContext, int retailer_weekly_sku_item, ArrayList<GetSetProgram> retailer_weekly_sku_model) {
            this.applicationContext=applicationContext;
            this.retailer_weekly_sku_model=retailer_weekly_sku_model;
            this.retailer_weekly_sku_item=retailer_weekly_sku_item;
        }

        @Override
        public ProgramListRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.program_list_recycler, parent, false);
            ProgramListRecycler.ViewHolder viewHolder=new ProgramListRecycler.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProgramListRecycler.ViewHolder holder, final int position) {

 Picasso.with(getApplicationContext()).load("http://mla-admin.sitsald.co.in/"+retailer_weekly_sku_model.get(position).getPimage()).into(holder.pimage);



holder.crashcourse.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
Intent i = new Intent(getApplicationContext(),ViewFullImage.class);
i.putExtra("image","http://mla-admin.sitsald.co.in/"+retailer_weekly_sku_model.get(position).getPimage());
startActivity(i);
    }
});


        }

        @Override
        public int getItemCount() {
            return retailer_weekly_sku_model.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView pimage;

            CardView crashcourse;
            public ViewHolder(View itemView) {
                super(itemView);
crashcourse=itemView.findViewById(R.id.crash_image);
                pimage=itemView.findViewById(R.id.p_image);



            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    void eventsHelper(final String Month) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Holidays.this);
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
                getCalEvents(Month);
                return null;
            }
        }.execute();
    }

}


