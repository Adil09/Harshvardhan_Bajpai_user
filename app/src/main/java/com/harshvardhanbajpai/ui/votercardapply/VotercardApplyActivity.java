package com.harshvardhanbajpai.ui.votercardapply;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.harshvardhanbajpai.AppController;



import com.harshvardhanbajpai.GetSetStateList;
import com.harshvardhanbajpai.OTPPage;
import com.harshvardhanbajpai.R;
import com.harshvardhanbajpai.Register;
import com.harshvardhanbajpai.model.GetSetAreaList;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotercardApplyActivity extends AppCompatActivity {

    LinearLayout toolbar_title_back;
    TextView text;
    Context context;
    String mla_id,user_id;
    LinearLayout selectcity_lineardrop,selectarea_lineardrop;
    Spinner spi_state, UR_spinner_city, spi_area;
    ArrayList<String> district_list=new ArrayList<>();
    String selectdistict_str="";
    EditText userdiss, username_voter, voter_mobile, voter_alternatemobile, textarea_et;
    private ArrayList<GetSetStateList> state_list=new ArrayList<>();
    String areaname="",subareid="",votercardaction="";
    RadioButton addition,modification,deletion;
    Button button_submit;
    ArrayList<GetSetAreaList>area_list=new ArrayList<>();
    String subarelistname="",subarelistid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votercard_apply);
        context= VotercardApplyActivity.this;

        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        SharedPreferences sss = getSharedPreferences("MLA_id", MODE_PRIVATE);
        mla_id = sss.getString("mla_id", "N/A");

        text = findViewById(R.id.text);

        call_method(user_id);

        modification=findViewById(R.id.modification);
        button_submit=findViewById(R.id.button_submit);
        username_voter=findViewById(R.id.username_voter);
        textarea_et=findViewById(R.id.textarea_et);
        voter_alternatemobile=findViewById(R.id.voter_alternatemobile);
        voter_mobile=findViewById(R.id.voter_mobile);
        deletion=findViewById(R.id.deletion);
        spi_area=findViewById(R.id.UR_spinner_subarea);
        addition=findViewById(R.id.addition);
        userdiss=findViewById(R.id.userdiss);
        spi_state=findViewById(R.id.UR_spinner_area);
        UR_spinner_city=findViewById(R.id.UR_spinner_city);
        selectarea_lineardrop=findViewById(R.id.selectarea_lineardrop);
        selectcity_lineardrop=findViewById(R.id.selectcity_lineardrop);

        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        district_list.add("Prayagraj");
        district_list.add("Other");

        getStateDetails("");

        setdistrictData(district_list, UR_spinner_city);

        UR_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectdistict_str = UR_spinner_city.getSelectedItem().toString();
                if (selectdistict_str.trim().equalsIgnoreCase("prayagraj")){
                    userdiss.setVisibility(View.GONE);
                    selectarea_lineardrop.setVisibility(View.VISIBLE);

                }else if (selectdistict_str.trim().equalsIgnoreCase("other")){
                    userdiss.setVisibility(View.VISIBLE);
                    selectarea_lineardrop.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spi_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    subareid = state_list.get(adapterView.getSelectedItemPosition()).getId();
                    areaname = state_list.get(adapterView.getSelectedItemPosition()).getName();
                    getAreaDetails(subareid);
                  //  Log.d("checkiddd", "onItemSelected: " + subareid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //City List
        spi_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    subarelistid = area_list.get(adapterView.getSelectedItemPosition()).getId();
                    subarelistname = area_list.get(adapterView.getSelectedItemPosition()).getName();

//                    Log.d(TAG, "onItemSelected: " + id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votercardaction="addition";

            }
        });
        modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votercardaction="modification";

            }
        });
        deletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votercardaction="deletion";

            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    if (subareid.equalsIgnoreCase("")){

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setMessage("Please Select Area");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();

                            }
                        });

                        AlertDialog alert=builder.create();
                        alert.show();


                     /*   if(subareid.equalsIgnoreCase("")){

                        }else {
                            userdiss.setText("");
                            send_details();
                        }*/

                    }else if (subarelistid.equalsIgnoreCase("")){

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setMessage("Please Select Sub-Area");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();

                            }
                        });

                        AlertDialog alert=builder.create();
                        alert.show();

                      /*  if(userdiss.getText().toString().isEmpty()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            builder.setMessage("Enter Your District Name");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();

                                }
                            });

                            AlertDialog alert=builder.create();
                            alert.show();
                        }else {
                            subareid="";
                            send_details();
                        }*/
                    }else {
                       // subareid="";
                       // send_details();
                        if (areaname.equalsIgnoreCase("select area")){
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            builder.setMessage("Please Select Area");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();

                                }
                            });

                            AlertDialog alert=builder.create();
                            alert.show();

                        }else if(subarelistname.equalsIgnoreCase("select subarea")){
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            builder.setMessage("Please Select Sub-Area");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();

                                }
                            });

                            AlertDialog alert=builder.create();
                            alert.show();

                        }else {
                             send_details();
                        }
                    }

                }

            }
        });
    }

    private void send_details() {

        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/addVoter.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg=jsonObject.getString("message");
                    Boolean error= jsonObject.optBoolean("error");

                    if (!error){

                        FancyToast.makeText(getApplicationContext(),"Submit Successfully",FancyToast.LENGTH_LONG).show();
                        onBackPressed();

                    }else {
                        FancyToast.makeText(getApplicationContext(),"Failed!!!",FancyToast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                  //  e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error.toString().equals("com.android.volley.AuthFailureError")) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("user_id", mla_id);
                params.put("contact_person",username_voter.getText().toString().trim());
                params.put("mobile",voter_mobile.getText().toString().trim());
                params.put("alt_mobile",voter_alternatemobile.getText().toString().trim());
                params.put("request", votercardaction);
                params.put("subarea_id", subarelistid);
                params.put("area_id", subareid);
               // params.put("subarea_id",subareid);
               // params.put("area_id", selectdistict_str);
              //  params.put("district",userdiss.getText().toString().trim()); // this could be blank
                params.put("district",""); // this could be blank
                params.put("description",textarea_et.getText().toString().trim());
                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    private void getAreaDetails(final String id) {


      /*  final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait  ...");
        progressDialog.show();*/
        final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_sub_area/"+id+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                area_list.clear();
               // progressDialog.dismiss();
                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    area_list.clear();
                    area_list.add(new GetSetAreaList(" ","Select Subarea"));
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("BoothSubArea");

                        area_list.add(new GetSetAreaList(packagedata.getString("area_id"), packagedata.getString("sub_area_in_english")));
                    }
                    if (area_list.size() > 0) setcityData(area_list, spi_area);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();
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

    private void getStateDetails(final String id) {
        state_list.clear();

        final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_area.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    state_list.clear();
                    state_list.add(new GetSetStateList(" ","Select Area"));
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("BoothArea");

                        state_list.add(new GetSetStateList(packagedata.getString("id"), packagedata.getString("area_in_english")));
                    }
                    if (state_list.size() > 0)
                        setstateData(state_list, spi_state);

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

    private void setstateData(ArrayList<GetSetStateList> items, Spinner spinner) {
        ArrayAdapter<GetSetStateList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }

    private void setcityData(ArrayList<GetSetAreaList> items, Spinner spinner) {
        ArrayAdapter<GetSetAreaList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }

    private void setdistrictData(ArrayList<String> items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }

    private void call_method(final String lan) {
        if (lan.equals("English")) {
            text.setText("APPLY VOTER CARD");

            // send_eng();
        } else if (lan.equals("Hindi")) {
            // send_hindi();
            text.setText("आवेदन मतदाता कार्ड");


        }
    }

    private boolean validation() {

        boolean validated = false;
        if(username_voter.getText().toString().isEmpty()){
            username_voter.requestFocus();
            username_voter.setError("Required");

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Enter Name");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();

        }else if(voter_mobile.length() > 10 || voter_mobile.length() < 10){

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Enter Mobile Number");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                    voter_mobile.requestFocus();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();
        }else if(voter_alternatemobile.length() > 10 || voter_alternatemobile.length() < 10){

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Enter Alternate Mobile Number");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                    voter_alternatemobile.requestFocus();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();
        }/*else  if(userdiss.getText().toString().isEmpty()){
            AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);
            builder.setCancelable(true);
            builder.setMessage("Enter Your District Name");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();

                }
            });

            AlertDialog alert=builder.create();
            alert.show();
        }*/
        /*else if(subareid.equals("")){
            AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);
            builder.setCancelable(true);
            builder.setMessage("Please Select Area");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();

                }
            });

            AlertDialog alert=builder.create();
            alert.show();
           // FancyToast.makeText(getApplicationContext(),"Please Select Area",FancyToast.LENGTH_LONG).show();
        }*/else if(votercardaction.equalsIgnoreCase("")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Please Choose Action on Voter Card");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();

                }
            });

            AlertDialog alert=builder.create();
            alert.show();

        }else if(textarea_et.getText().toString().isEmpty()){
            textarea_et.requestFocus();
            textarea_et.setError("Required");

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Please type your comment");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();

        }


        else {
            validated = true;
        }
        return validated;
    }
}