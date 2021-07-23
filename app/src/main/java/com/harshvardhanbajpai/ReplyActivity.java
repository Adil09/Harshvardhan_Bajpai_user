package com.harshvardhanbajpai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ReplyActivity extends AppCompatActivity {
    String uid, sub;
    ImageView support_arrow;
    String id, Type, date, Subject;
    TextView subject_reply, type_comm, date_time;
    RecyclerView replyRecycler;
    ArrayList<ReplyModel> replyModels = new ArrayList<>();
    Button add_new_reply;
    EditText reply_text;
    String register;
    TextView text_subarea,text_category,processing,viewname;
String user_id, description,subject;
    TextView subject_tv, description_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sss = getSharedPreferences("MLA_id", MODE_PRIVATE);
        register = sss.getString("mla_id", "N/A");

        Bundle bundle= getIntent().getExtras();
        if (bundle != null){

            subject= bundle.getString("subject");
            description= bundle.getString("description");
        }

        SharedPreferences s = getSharedPreferences("Detail", MODE_PRIVATE);
        id = s.getString("Id", "N/A");
        Type = s.getString("Type", "N/A");
        date = s.getString("Date", "N/A");
        Subject = s.getString("Subject", "N/A");

        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");


        subject_reply = findViewById(R.id.subject_com);
        type_comm = findViewById(R.id.type_comm);
        date_time = findViewById(R.id.date_time);
        replyRecycler = findViewById(R.id.replyRecycler);
        add_new_reply = findViewById(R.id.add_new_reply);
        processing=findViewById(R.id.processing);
        reply_text = findViewById(R.id.reply_text);
        text_category=findViewById(R.id.text_category);
        subject_tv=findViewById(R.id.subject_tv);
        description_tv=findViewById(R.id.description_tv);
        text_subarea=findViewById(R.id.text_subarea);
        viewname=findViewById(R.id.viewname);
        type_comm.setText("" + Type);
        date_time.setText("" + date);
        subject_reply.setText("" + Subject);
        if(Type.equals("null"))
        {
            text_category.setVisibility(View.GONE);
            type_comm.setVisibility(View.GONE);
        }
        support_arrow = findViewById(R.id.support_reply);
        support_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        add_new_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) {
                    send_data(reply_text.getText().toString().trim());
                    add_new_reply.setClickable(false);
                }
            }
        });
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                view_data(id);
                ha.postDelayed(this, 6000);
            }
        }, 6000);

        subject_tv.setText(subject);
        description_tv.setText(description);

        view_data(id);
        call_method(user_id);
    }

    private boolean validation() {

        boolean validated = false;
        if(reply_text.getText().toString().isEmpty()){
            reply_text.requestFocus();
            reply_text.setError("Required");
        }
        else {
            validated = true;
        }
        return validated;
    }
    private void call_method(final String lan) {
        if(lan.equals("English")){
            processing.setText("Your Complaint is Processing");
           text_category.setText("Category");
            text_subarea.setText("Area ");
            viewname.setText("View Detail");

            // send_eng();
        }else if(lan.equals("Hindi")) {
            // send_hindi();
            processing.setText("आपकी शिकायत संसाधित हो रही है !");
            viewname.setText("डिटेल देखे");
            text_subarea.setText("एरिया");
            text_category.setText("केटेगरी");

        }
    }
    private void send_data(final String message) {
        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        final String datetime = dateformat.format(c.getTime());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/replyComplain.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetURLComplain", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("message");

                    boolean error = jsonObject1.getBoolean("error");
                    if (!error) {
                        add_new_reply.setClickable(true);
                        reply_text.setText(" ");
                        FancyToast.makeText(getApplicationContext(), jsonObject1.optString("message"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        view_data(id);
                    } else if (error)
                        Toast.makeText(getApplicationContext(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof ServerError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof AuthFailureError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof ParseError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof NoConnectionError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof TimeoutError)
                    FancyToast.makeText(getApplicationContext(), "Connection TimeOut! ...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("complain_id", id);
                hashMap.put("reply_from", register);
                hashMap.put("reply_to", "Admin");
                hashMap.put("reply",message);
                hashMap.put("reply_date",datetime);

                return hashMap;
            }
        };
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    private void view_data(final String com_id) {
        replyModels.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_complainDetails/"+com_id+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("message");
                    boolean error = jsonObject1.optBoolean("error");
                    if (!error) {
                        replyRecycler.setVisibility(View.VISIBLE);
                        processing.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject1.getJSONArray("complaint_answers");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonOb = jsonArray.optJSONObject(i);
                            JSONObject jsonObject2=jsonOb.getJSONObject("ComplainAnswer");
                            String id = jsonObject2.optString("id");
                            String reply = jsonObject2.optString("reply");
                            String reply_to = jsonObject2.optString("reply_from");
                            String reply_date = jsonObject2.optString("reply_date");
                            replyModels.add(new ReplyModel(id, reply, reply_to, reply_date));
                        }
                        ReplyRecycler supportRecycler = new ReplyRecycler((ReplyActivity.this), R.layout.reply_recycler_data, replyModels);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ReplyActivity.this, 1);
                        replyRecycler.setLayoutManager(mLayoutManager);
                        replyRecycler.scrollToPosition(replyModels.size()-1);
                        replyRecycler.setHasFixedSize(true);
                        replyRecycler.setAdapter(supportRecycler);
                    } else {
                        replyRecycler.setVisibility(View.GONE);
                        processing.setVisibility(View.VISIBLE);
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
                HashMap<String, String> hashMap = new HashMap<>();

                return hashMap;
            }
        };
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    private class ReplyRecycler extends RecyclerView.Adapter<ReplyRecycler.ViewHolder> {
        Context applicationContext;
        int retailer_listing_item;
        ArrayList<ReplyModel> replyModels;

        public ReplyRecycler(Context applicationContext, int retailer_listing_item, ArrayList<ReplyModel> supportModels) {
            this.applicationContext = applicationContext;
            this.retailer_listing_item = retailer_listing_item;
            this.replyModels = supportModels;
        }

        @Override
        public ReplyRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.reply_recycler_data, parent, false);
            ReplyRecycler.ViewHolder viewHolder = new ReplyRecycler.ViewHolder(view);
            return viewHolder;
        }



        @Override
        public void onBindViewHolder(ReplyRecycler.ViewHolder holder, final int position) {

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateForma = new SimpleDateFormat("MMMM");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-DD");
            DateFormat dateFormat11 = new SimpleDateFormat("dd");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm ");
            SimpleDateFormat sdfs1 = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
            String w = replyModels.get(position).getReplyto();

            String replydate = replyModels.get(position).getDate();


            if (w.equalsIgnoreCase("null") && replydate.equalsIgnoreCase("null")) {
                holder.to.setVisibility(View.GONE);
                holder.me.setVisibility(View.GONE);
            } else {

                if (w.equalsIgnoreCase(register)) {

                    Log.d("SUPORT", "onBindViewHolder: me  " + replyModels.get(holder.getAdapterPosition()).getReply());
                    holder.to.setVisibility(View.VISIBLE);
                    holder.me.setVisibility(View.GONE);
                    holder.replyto.setText("" + replyModels.get(position).getReply());
                    holder.replyto_to.setText(replydate);
//                holder.topview_linear.setGravity(Gravity.START);
//                holder.topview_linear.setBackgroundColor(Color.parseColor("#FFC5CE4C"));
                } else {
                    Log.d("SUPORT", "onBindViewHolder: admin" + replyModels.get(holder.getAdapterPosition()).getReply());
                    holder.to.setVisibility(View.GONE);
                    holder.me.setVisibility(View.VISIBLE);
                    holder.textView.setText("" + replyModels.get(position).getReply());
                    holder.textview2.setText(replydate);
//
//                holder.topview_linear.setGravity(Gravity.END);
//                holder.textview2.append(w + " : " + " " + replydate);
                }
//            holder.topview_linear.setLayoutParams(params);
            }
        }
        @Override
        public int getItemCount() {
            return replyModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView, textview2, replyto, replyto_to;
            LinearLayout topview_linear;
            LinearLayout me, to;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.Reply);
                textview2 = itemView.findViewById(R.id.Reply_To);
                topview_linear = itemView.findViewById(R.id.topview_linear);

                me = itemView.findViewById(R.id.me);
                to = itemView.findViewById(R.id.to);
                replyto = itemView.findViewById(R.id.Replyto_t0);
                replyto_to = itemView.findViewById(R.id.Reply_To_to);


            }
        }
    }
}