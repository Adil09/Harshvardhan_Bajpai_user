package com.harshvardhanbajpai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Questions extends AppCompatActivity {
    LinearLayout toolbar_title_back;
    String user_id, memberuid;
    String subareid="",subarelistid="";
TextView text,ares;
EditText com_detail,com_sub;
Button get_start_send;
    Spinner spi_create,spi_create1;
    ArrayList<GetSetAreaList> area_list=new ArrayList<>();
    private ArrayList<GetSetStateList> state_list=new ArrayList<>();
  ProgressDialog progressDialog;
  String Response_image="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");

        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        memberuid = s.getString("mla_id", "N/A");

        ares = findViewById(R.id.ares);
        text=findViewById(R.id.text);
        com_detail = findViewById(R.id.com_detail);

        com_sub = findViewById(R.id.com_sub);

        get_start_send = findViewById(R.id.get_start_send);
get_start_send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        if(validation())
            startThread();
    }
});
        spi_create=findViewById(R.id.UR_spinner_area);
        spi_create1=findViewById(R.id.UR_spinner_subarea);
        spi_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    subareid = state_list.get(adapterView.getSelectedItemPosition()).getId();
                    getAreaDetails(subareid);
                    Log.d("checkiddd", "onItemSelected: " + subareid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //City List
        spi_create1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    subarelistid = area_list.get(adapterView.getSelectedItemPosition()).getId();



//                    Log.d(TAG, "onItemSelected: " + id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getStateDetails("");

call_method(user_id);
    }

    private boolean validation() {

        boolean validated = false;
        if(com_sub.getText().toString().isEmpty()){
if(user_id.equals("English")) {
    AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
    builder.setCancelable(true);
    builder.setMessage("Enter Subject");
    builder.setInverseBackgroundForced(true);
    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            com_sub.requestFocus();

        }
    });

    AlertDialog alert = builder.create();
    alert.show();
}else {
    AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
    builder.setCancelable(true);
    builder.setMessage("सबजेक्ट लिखे");
    builder.setInverseBackgroundForced(true);
    builder.setPositiveButton("ओके ", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            com_sub.requestFocus();

        }
    });

    AlertDialog alert = builder.create();
    alert.show();
}
        }else if(com_detail.getText().toString().isEmpty()) {
            if (user_id.equals("English")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                builder.setCancelable(true);
                builder.setMessage("Enter Complain Description");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        com_detail.requestFocus();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                builder.setCancelable(true);
                builder.setMessage("कृपया सवाल लिखे");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("ओके ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        com_detail.requestFocus();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        else {
            validated = true;
        }
        return validated;
    }

    @SuppressLint("StaticFieldLeak")
    public void startThread() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Questions.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                Response_image = QuestionConfi.uploadImage("","","","",com_sub.getText().toString().trim(),
                        memberuid,com_detail.getText().toString().trim(),"http://mla-admin.sitsald.co.in/users/save_complain.json","QA");
//             Log.d("ApiImageResponse", Response_image);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    progressDialog.dismiss();

                    if (!TextUtils.isEmpty(Response_image)) {
                        JSONObject jsonObject = new JSONObject(Response_image);
                        JSONObject jsonObject1=jsonObject.getJSONObject("message");
                        if (jsonObject1.getString("message").equals("Complaint Saved")) {
                            // FancyToast.makeText(Confidantal.this, jsonObject1.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,  false).show();
                            if (user_id.equals("English")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                                builder.setMessage("Your Vidhansabha Question has been sent.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // do nothing
                                                Intent i = new Intent(getApplicationContext(), Home.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }else if (user_id.equals("Hindi")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                                builder.setMessage("आपका विधान प्रश्न भेजा गया है ।")
                                        .setCancelable(false)
                                        .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // do nothing
                                                Intent i = new Intent(getApplicationContext(), Home.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }

                        } else
                            FancyToast.makeText(Questions.this, jsonObject.getString("msg"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    private void getStateDetails(final String id) {
        state_list.clear();

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_area.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    state_list.clear();
                    if(user_id.equals("English")) {
                        state_list.add(new GetSetStateList(" ", "Select Area"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("BoothArea");

                            state_list.add(new GetSetStateList(packagedata.getString("id"), packagedata.getString("area_in_english")));
                        }
                    }else if(user_id.equals("Hindi")){
                        state_list.add(new GetSetStateList(" ", "एरिया चुने"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("BoothArea");

                            state_list.add(new GetSetStateList(packagedata.getString("id"), packagedata.getString("area_in_hindi")));
                        }
                    }
                    if (state_list.size() > 0)
                        setstateData(state_list, spi_create);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
                else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
                else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof TimeoutError)
                    FancyToast.makeText(getApplicationContext(), "Connection TimeOut! ...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
            }
        });
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        AppController.getInstance().addToReqQueue(stringRequest);
    }


    private void getAreaDetails(final String id) {

        area_list.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Questions.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait  ...");
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_sub_area/"+id+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    area_list.clear();
                    if(user_id.equals("English")) {
                        area_list.add(new GetSetAreaList(" ", "Select Subarea"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("BoothSubArea");

                            area_list.add(new GetSetAreaList(packagedata.getString("area_id"), packagedata.getString("sub_area_in_english")));
                        }
                    }else if(user_id.equals("Hindi")){
                        area_list.add(new GetSetAreaList(" ", "सबएरिया चुने"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("BoothSubArea");
                            String result = URLDecoder.decode(packagedata.getString("sub_area_in_hindi"), "UTF-8");
                            area_list.add(new GetSetAreaList(packagedata.getString("area_id"), result));
                        }
                    }
                    if (area_list.size() > 0) setcityData(area_list, spi_create1);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof NetworkError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
                else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError)
                    FancyToast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
                else if (error instanceof TimeoutError)
                    FancyToast.makeText(getApplicationContext(), "Connection TimeOut! ...Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.INFO,  false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> getParams = new HashMap<>();

                return getParams;
            }
        };
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        AppController.getInstance().addToReqQueue(stringRequest);
    }
    private void setcityData(ArrayList<GetSetAreaList> items, Spinner spinner) {
        ArrayAdapter<GetSetAreaList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }
    private void setstateData(ArrayList<GetSetStateList> items, Spinner spinner) {
        ArrayAdapter<GetSetStateList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }
    private void call_method(final String lan) {
        if (lan.equals("English")) {

            text.setText("QUESTION");
            ares.setText("Select Area and Subarea");
            com_sub.setHint("Enter Subject");
            com_detail.setHint("Enter Question");

            get_start_send.setText("Post Your Question");


        } else if (lan.equals("Hindi")) {
            com_sub.setHint("सबजेक्ट लिखे");
            text.setText("सवाल");
            ares.setText("एरिया ओर सबएरिया चुने ");

            com_detail.setHint("कृपया सवाल लिखे ");

            get_start_send.setText("सवाल दर्ज कराए  ");

        }
    }
}
