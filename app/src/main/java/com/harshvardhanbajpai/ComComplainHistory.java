package com.harshvardhanbajpai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ComComplainHistory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
String memberuid;

ArrayList<MyCompListModel>my_order_recycler=new ArrayList<>();
RecyclerView comp;
LinearLayout toolbar_title_back;
TextView text;
String user_id;
SwipeRefreshLayout swipeRefreshLayout;
TextView nocom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_history);


        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        memberuid = s.getString("mla_id", "N/A");
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        comp=findViewById(R.id.comp);
        text=findViewById(R.id.text);
        nocom=findViewById(R.id.nocom);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout1);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        package_list();


                                        call_method(user_id);


                                    }
                                }
        );



    }
    @Override
    public void onRefresh() {

        package_list();


        call_method(user_id);

    }
    private void call_method(final String lan) {
        if(lan.equals("English")){

            text.setText("CONFIDENTIAL COMPLAINT HISTORY");
            nocom.setText("NO COMPLAINT");

        }else if(lan.equals("Hindi")) {

            text.setText("कॉन्फिडेंटिअल शिकायत हिस्ट्री");

            nocom.setText("कोई शिकायत नहीं है");
        }
    }
    private void package_list() {
        swipeRefreshLayout.setRefreshing(true);
        my_order_recycler.clear();
        final ProgressDialog progressDialog = new ProgressDialog(ComComplainHistory.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_complaints/"+memberuid+"/0/confidential"+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("myorder",response);
                progressDialog.dismiss();

                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");
boolean error=jsonObject.getBoolean("error");
if(!error){
    nocom.setVisibility(View.GONE);
                    JSONArray user = jsonObject.getJSONArray("complaints");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("Complaint");
                        JSONObject userdata = loopdata.getJSONObject("ComCategory");
                        JSONObject ComSubCategory = loopdata.getJSONObject("ComSubCategory");
                        JSONObject BoothArea = loopdata.getJSONObject("BoothArea");
                        JSONObject BoothSubArea = loopdata.getJSONObject("BoothSubArea");

                        if (user_id.equals("English")) {

                            my_order_recycler.add(new MyCompListModel(packagedata.getString("id"), packagedata.getString("com_image"), packagedata.getString("created"), packagedata.getString("com_video"), packagedata.getString("com_type"), packagedata.getString("com_text"),

                                    BoothArea.getString("area_in_english"), BoothSubArea.getString("sub_area_in_english"),

                                    userdata.getString("name_in_english"), ComSubCategory.getString("name_in_english")));

                        } else if (user_id.equals("Hindi")) {

                            my_order_recycler.add(new MyCompListModel(packagedata.getString("id"), packagedata.getString("com_image"), packagedata.getString("created"), packagedata.getString("com_video"), packagedata.getString("com_type"), packagedata.getString("com_text"),

                                    BoothArea.getString("area_in_hindi"), BoothSubArea.getString("sub_area_in_hindi"),

                                    userdata.getString("name_in_hindi"), ComSubCategory.getString("name_in_hindi")));
                        }

                    }
                    }else {
    nocom.setVisibility(View.VISIBLE);
                    }
                    if (my_order_recycler.size() > 0) {

                        comp.setLayoutManager(new LinearLayoutManager(ComComplainHistory.this, LinearLayoutManager.VERTICAL, false));
                        comp.setAdapter(new PackageRecycler(ComComplainHistory.this, R.layout.my_order_recycler, my_order_recycler));

//
//


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();

                return hashMap;
            }
        };
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private class PackageRecycler extends RecyclerView.Adapter<PackageRecycler.ViewHolder>{

        Context applicationContext;
        int retailer_weekly_sku_item;
        ArrayList<MyCompListModel> retailer_weekly_sku_model;

        public PackageRecycler(Context applicationContext, int retailer_weekly_sku_item, ArrayList<MyCompListModel> retailer_weekly_sku_model) {
            this.applicationContext=applicationContext;
            this.retailer_weekly_sku_model=retailer_weekly_sku_model;
            this.retailer_weekly_sku_item=retailer_weekly_sku_item;
        }

        @Override
        public PackageRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.my_order_recycler, parent, false);
            PackageRecycler.ViewHolder viewHolder=new PackageRecycler.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PackageRecycler.ViewHolder holder, final int position) {

           // Picasso.with(getApplicationContext()).load("https://app-admin.kwalityhosting.com/package_image/original/"+retailer_weekly_sku_model.get(position).getP_cover_photo()).into(holder.imh);

            if(user_id.equals("English")){
                holder.subarename.setText(retailer_weekly_sku_model.get(position).getSub_area_in_english());

                holder.areaname.setText(retailer_weekly_sku_model.get(position).getArea_in_english());


                holder.subcatarename.setText(retailer_weekly_sku_model.get(position).getSubcat_in_english1());

                // holder.texttt.setText(retailer_weekly_sku_model.get(position).getCom_text());
                holder.values.setText(retailer_weekly_sku_model.get(position).getId());
                holder.texttt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Detail",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("Id",retailer_weekly_sku_model.get(position).getComid()).commit();
                        editor.putString("Type",retailer_weekly_sku_model.get(position).getSubcat_in_english1()).commit();
                        editor.putString("Date",retailer_weekly_sku_model.get(position).getCreated()).commit();
                        editor.putString("Subject",retailer_weekly_sku_model.get(position).getArea_in_english()).commit();
                        Intent i = new Intent(getApplicationContext(),ReplyActivity.class);

                        startActivity(i);
                    }
                });
                holder.valuesdate.setText(retailer_weekly_sku_model.get(position).getCreated());
                holder.text_area.setText("Area");
holder.text_subarea.setText("Sub-Area");

                holder.text_subcate.setText("Category ");
                holder.texttt_video.setText("View Video");
                holder.texttt.setText("View Detail");
                holder.textvalue.setText("Value ");
                holder.textvaluedate.setText("Date");
                holder.texttt_image.setText("View Photo");

            }else if(user_id.equals("Hindi")) {
                holder.text_area.setText("एरिया");
holder.text_subarea.setText("सबएरिया");

                holder.text_subcate.setText("केटेगरी ");
                holder.texttt_video.setText("वीडियो देखे");
                holder.texttt_image.setText("फोटो देखे");
                holder.texttt.setText("डिटेल देखे");
                holder.textvaluedate.setText("सिकायत दिनांक ");
                holder.valuesdate.setText(retailer_weekly_sku_model.get(position).getCreated());
                holder.subarename.setText(retailer_weekly_sku_model.get(position).getSub_area_in_english());

                holder.areaname.setText(retailer_weekly_sku_model.get(position).getArea_in_english());


                holder.subcatarename.setText(retailer_weekly_sku_model.get(position).getSubcat_in_english1());

                // holder.texttt.setText(retailer_weekly_sku_model.get(position).getCom_text());

                holder.texttt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Detail",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("Id",retailer_weekly_sku_model.get(position).getComid()).commit();
                        editor.putString("Type",retailer_weekly_sku_model.get(position).getSubcat_in_english1()).commit();
                        editor.putString("Date",retailer_weekly_sku_model.get(position).getCreated()).commit();
                        editor.putString("Subject",retailer_weekly_sku_model.get(position).getArea_in_english()).commit();
                        Intent i = new Intent(getApplicationContext(),ReplyActivity.class);

                        startActivity(i);
//                        AlertDialog.Builder builder=new AlertDialog.Builder(ComplainHistory.this);
//                        builder.setCancelable(true);
//                        builder.setTitle(retailer_weekly_sku_model.get(position).getCat_in_english());
//                        builder.setMessage(retailer_weekly_sku_model.get(position).getCom_text());
//                        builder.setInverseBackgroundForced(true);
//                        builder.setPositiveButton("ओके",new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which){
//                                dialog.dismiss();
//
//                            }
//                        });
//
//                        AlertDialog alert=builder.create();
//                        alert.show();
                    }
                });
holder.textvalue.setText("वैल्यू ");
                if(retailer_weekly_sku_model.get(position).getId().equals("normal")){
                    holder.values.setText("नोरमल ");

                    holder.crashcourse.setCardBackgroundColor(Color.parseColor("#F6F6FB"));
                }else if(retailer_weekly_sku_model.get(position).getId().equals("confidential")){
                    holder.values.setText("कॉन्फिडेंटिअल");

                    holder.crashcourse.setCardBackgroundColor(Color.parseColor("#E5E6FA"));
                }else {

                }

            }
            if(retailer_weekly_sku_model.get(position).getCom_video().equals("")){
                holder.texttt_video.setVisibility(View.GONE);
            }else {
                holder.texttt_video.setVisibility(View.VISIBLE);
                holder.texttt_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), VideoShow.class);
                        i.putExtra("news", retailer_weekly_sku_model.get(position).getSubcat_in_english1());
                        i.putExtra("name", retailer_weekly_sku_model.get(position).getCom_video());
                        startActivity(i);
                    }
                });
            }
if(retailer_weekly_sku_model.get(position).getCom_image().equals("")){
    holder.texttt_image.setVisibility(View.GONE);
}else {
    holder.texttt_image.setVisibility(View.VISIBLE);
    holder.texttt_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), VideoShow.class);
            i.putExtra("news",retailer_weekly_sku_model.get(position).getSubcat_in_english1());
            i.putExtra("name",retailer_weekly_sku_model.get(position).getCom_image());
            startActivity(i);
        }
    });
}


        }

        @Override
        public int getItemCount() {
            return retailer_weekly_sku_model.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subarename,areaname,subcatarename;
TextView text_area,text_subarea,text_category,text_subcate,textvaluedate;
            TextView texttt,values,texttt_video,textvalue,valuesdate,texttt_image;

CardView crashcourse;
            public ViewHolder(View itemView) {
                super(itemView);
                texttt_image=itemView.findViewById(R.id.texttt_image);
                textvaluedate=itemView.findViewById(R.id.textvaluedate);
                valuesdate=itemView.findViewById(R.id.valuesdate);
                textvalue=itemView.findViewById(R.id.textvalue);
                text_subcate=itemView.findViewById(R.id.text_subcate);
                text_category=itemView.findViewById(R.id.text_category);
                text_subarea=itemView.findViewById(R.id.text_subarea);
                text_area=itemView.findViewById(R.id.text_area);
                texttt_video=itemView.findViewById(R.id.texttt_video);
                values=itemView.findViewById(R.id.values);
                subarename=itemView.findViewById(R.id.subarename);
                areaname=itemView.findViewById(R.id.areaname);
                crashcourse=itemView.findViewById(R.id.crashcourse);

                subcatarename=itemView.findViewById(R.id.subcatarename);
                texttt=itemView.findViewById(R.id.texttt);


            }
        }
    }

}
