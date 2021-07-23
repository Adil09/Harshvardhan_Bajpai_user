package com.harshvardhanbajpai;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
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
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConMessage extends AppCompatActivity {
    LinearLayout support_arrow;
    String id, Type, date, Subject;
    TextView subject_reply, type_comm, date_time;
    RecyclerView replyRecycler;
    ArrayList<ReplyModel> replyModels = new ArrayList<>();
    Button add_new_reply;
    EditText reply_text;
    String register;
    TextView text,text_category,processing,viewname;
    String user_id;
    String convTime = null;
    final Handler handler = new Handler();//class variable
    int count = 0;
    TextView text_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_message);
        SharedPreferences sss = getSharedPreferences("MLA_id", MODE_PRIVATE);
        register = sss.getString("mla_id", "N/A");



        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        support_arrow = findViewById(R.id.toolbar_title_back);
        support_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        replyRecycler=findViewById(R.id.replyRecycler);
        text=findViewById(R.id.text);
        text_msg=findViewById(R.id.text_msg);
        reply_text=findViewById(R.id.reply_text);
        processing=findViewById(R.id.processing);
        add_new_reply=findViewById(R.id.add_new_reply);
        add_new_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) {
                    send_data(reply_text.getText().toString().trim());
                    add_new_reply.setClickable(false);
                }
            }
        });


        handler.post(new Runnable() {

            @Override
            public void run() {

                updateCounter(count++);

                if(count < (10*5000)) {

                    handler.postDelayed(this, 5000);

                }

            }

        });
    }
    private void updateCounter(final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                call_method(user_id);
                view_data(register);
                // you have the seconds passed
                // do what ever you want
            }
        });
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
            processing.setText("NO CONFIDENTIAL MESSAGE");
           // text_category.setText("Category");
            text.setText("SEND CONFIDENTIAL MESSAGE");
          ;
text_msg.setText("This Message only can be read by Harsh Vardhan Bajpai");
            // send_eng();
        }else if(lan.equals("Hindi")) {
            // send_hindi();
            text.setText("गोपनीय संदेस भेजे");
            processing.setText("गोपनीय संदेस  है !");
            text_msg.setText("यह संदेश केवल हर्षवर्धन बाजपेयी ही पढ़ सकते हैं ");
           // viewname.setText("डिटेल देखे");


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
                        view_data(register);
                    } else if (error)
                        Toast.makeText(getApplicationContext(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
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

                hashMap.put("complain_id", register);
                hashMap.put("reply_from", register);
                hashMap.put("reply_to", "Admin");
                hashMap.put("reply",message);
                hashMap.put("reply_date",datetime);
                hashMap.put("reply_type","CM");

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
        replyRecycler.getRecycledViewPool().clear();

        replyRecycler.setItemAnimator(null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_complainDetails/"+com_id+"/CM.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("check_future_list", response);
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

                       // replyRecycler.scrollToPosition(replyModels.size()-1);
                       // replyRecycler.setHasFixedSize(true);


                    } else {
                        replyRecycler.setVisibility(View.GONE);
                        processing.setVisibility(View.VISIBLE);
                    }
                    if (replyModels.size() > 0) {
                        ReplyRecycler teamRecycler = new ReplyRecycler(ConMessage.this, R.layout.reply_recycler_data, replyModels);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ConMessage.this, 1);
                        replyRecycler.setLayoutManager(mLayoutManager);
                        replyRecycler.setAdapter(teamRecycler);
                        replyRecycler.scrollToPosition(replyModels.size()-1);
                        replyRecycler.setHasFixedSize(true);
                        teamRecycler.notifyItemRangeChanged(0, replyModels.size());



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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
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


                if (w.equalsIgnoreCase(register)) {

                    Log.d("SUPORT", "onBindViewHolder: me  " + replyModels.get(holder.getAdapterPosition()).getReply());
                    holder.to.setVisibility(View.VISIBLE);
                    holder.me.setVisibility(View.GONE);
                    holder.replyto.setText("" + replyModels.get(position).getReply());
                    DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = null;
                    try {
                        date = inputFormatter.parse(replydate);
                        DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String output = outputFormatter.format(date);

                        SimpleDateFormat dateFo = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        Date pasTime = dateFo.parse(output);
                        Log.d("gettime", String.valueOf(pasTime.getTime()));
                        covertTimeToText(replydate);

                        holder.replyto_to.setText("" + replydate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                } else {

                    holder.to.setVisibility(View.GONE);
                    holder.me.setVisibility(View.VISIBLE);
                    holder.textView.setText("" + replyModels.get(position).getReply());
                    holder.textview2.setText(""+replydate);

                }
//            holder.topview_linear.setLayoutParams(params);

        }
        public String covertTimeToText(String dataDate) {


            String prefix = "";
            String suffix = "Ago";

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                Date pasTime = dateFormat.parse(dataDate);

                Date nowTime = new Date();

                long dateDiff = nowTime.getTime() - pasTime.getTime();

                long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
                long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

                if (second < 60) {
                    convTime = second+" Seconds "+suffix;
                } else if (minute < 60) {
                    convTime = minute+" Minutes "+suffix;
                } else if (hour < 24) {
                    convTime = hour+" Hours "+suffix;
                } else if (day >= 7) {
                    if (day > 360) {
                        convTime = (day / 30) + " Years " + suffix;
                    } else if (day > 30) {
                        convTime = (day / 360) + " Months " + suffix;
                    } else {
                        convTime = (day / 7) + " Week " + suffix;
                    }
                } else if (day < 7) {
                    convTime = day+" Days "+suffix;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("ConvTimeE", e.getMessage());
            }

            return convTime;
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
