package com.harshvardhanbajpai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

public class Confidantal extends AppCompatActivity {
LinearLayout toolbar_title_back;

    Spinner spi_create,spi_create1;
    ArrayList<GetSetAreaList>area_list=new ArrayList<>();
    private ArrayList<GetSetStateList> state_list=new ArrayList<>();
    ArrayList<GetSetCategoryList>getSetCategoryLists=new ArrayList<>();
    private ArrayList<GetSetSubcategoryList> getSetSubcategoryLists=new ArrayList<>();

    String subareid="",subarelistid="";
    Spinner spi_cat,spi_subcat;
    String catid="",subcatid="";
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;
    private ProgressBar progressBar;
    private Activity activity;
    Button get_start,get_start_send,get_start2;
    TextView text;
    String user_id;
    EditText mobilee,com_detail,enter_name;
    TextView ares,ares_cat;
    String Response_image="",filePath="";
    ProgressDialog progressDialog;
    String memberuid;
    long totalSize = 0;
    String pathdis="";
    private String upload_URL = "http://mla-admin.sitsald.co.in/users/save_complain.json";
    private RequestQueue rQueue;
LinearLayout text_layut;
Uri uri;
String imageFileName="";
String displayName;
TextView videorecord,videorecord1;
LinearLayout get_start1,get_start11;
TextView videorecordtym;
    String confidential="confidential";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confidentalmessgage);
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
        activity = this;


        get_start=findViewById(R.id.get_start);
        get_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 15) {
                    askForPermissions(new String[]{
                                    android.Manifest.permission.CAMERA,
                                    android.Manifest.permission.RECORD_AUDIO,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CAMERA_PERMISSIONS);
                } else {
                    enableCamera();
                }
            }
        });

        get_start2=findViewById(R.id.get_start2);
        get_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageCameraActivity.launch(Confidantal.this);
            }
        });
        text=findViewById(R.id.text);
        enter_name=findViewById(R.id.enter_name);
        ares=findViewById(R.id.ares);
        ares_cat=findViewById(R.id.ares_cat);
        mobilee=findViewById(R.id.mobilee);
        text_layut=findViewById(R.id.text_layut);
        get_start1=findViewById(R.id.get_start1);
        get_start11=findViewById(R.id.get_start11);
        videorecordtym=findViewById(R.id.videorecordtym);
        get_start_send=findViewById(R.id.get_start_send);
        videorecord=findViewById(R.id.videorecord);
        videorecord1=findViewById(R.id.videorecord1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        com_detail=findViewById(R.id.com_detail);
call_method(user_id);
    }
    private void call_method(final String lan) {
        if(lan.equals("English")){
            enter_name.setHint("Enter Full Name");
            text.setText("SEND CONFIDENTIAL MESSAGE");
            ares.setText("Select Area and Subarea");
            mobilee.setHint("Enter Mobile Number");
            com_detail.setHint("Enter Complaint Details");
            get_start.setText("Record Video ");
            get_start2.setText("Capture Image");
            get_start_send.setText("Post Your Complaint");
            videorecord.setText("Video has been recorded");
            videorecord1.setText("Photo has been clicked");
            ares_cat.setText("Select Category ");
            videorecordtym.setText("Only 60 seconds of video will be recorded !");

        }else if(lan.equals("Hindi")) {
            enter_name.setHint("कृपया अपना पूरा नाम लिखे");
            text.setText("गोपनीय संदेस भेजे");
            ares_cat.setText("केटेगरी  चुने");
            ares.setText("एरिया ओर सबएरिया चुने ");
            mobilee.setHint("कृपया अपना मोबईल नंबर लिखे");
            com_detail.setHint("कृपया शिकायत लिखे ");
            get_start.setText("वीडियो रिकॉर्ड करे ");
            get_start2.setText("फोटो खीचे ");
            videorecord.setText("वीडियो रिकॉर्ड कर ली गयी है");
            videorecord1.setText("फोटो क्लिक कर ली गयी है");
            get_start_send.setText("शिकायत दर्ज कराए  ");
            videorecordtym.setText("केवल तीस सेकण्ड की वीडियो रिकॉर्ड होगी !");
        }

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


        spi_cat=findViewById(R.id.UR_spinner_cat);
        spi_subcat=findViewById(R.id.UR_spinner_subcat);
        spi_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    catid = getSetCategoryLists.get(adapterView.getSelectedItemPosition()).getId();

                    Log.d("checkiddd", "onItemSelected: " + catid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getsubcaDetails("");

        //City List
        spi_subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItemPosition() > 0) {
                    subcatid = getSetSubcategoryLists.get(adapterView.getSelectedItemPosition()).getId();


//                    Log.d(TAG, "onItemSelected: " + id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getCatDetails("");

        get_start_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com_detail.getText().toString().equals("")) {
                    com_detail.findFocus();
                    com_detail.setError("Required");
                    FancyToast.makeText(getApplicationContext(), "All Field are required", FancyToast.LENGTH_LONG).show();
                } else {
                  startThread();
                }
            }

        });
    }
    //
    private void getCatDetails(final String id) {
        getSetCategoryLists.clear();

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/com_category.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    getSetCategoryLists.clear();
                    if(user_id.equals("English")) {
                        getSetCategoryLists.add(new GetSetCategoryList(" ", "Select Category"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("ComCategory");

                            getSetCategoryLists.add(new GetSetCategoryList(packagedata.getString("id"), packagedata.getString("name_in_english")));
                        }
                    }else if(user_id.equals("Hindi")){
                        getSetCategoryLists.add(new GetSetCategoryList(" ", "केटेगरी चुने"));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("ComCategory");
                         String encodedString=  packagedata.getString("name_in_hindi");
                            getSetCategoryLists.add(new GetSetCategoryList(packagedata.getString("id"), encodedString));
                        }
                    }
                    if (getSetCategoryLists.size() > 0)
                        setcatData(getSetCategoryLists, spi_cat);

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

    @SuppressLint("StaticFieldLeak")
    public void startimage() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Confidantal.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                Response_image = ImageUpload.uploadImage(imageFileName,filePath,subareid,subarelistid,subcatid,
                        memberuid,com_detail.getText().toString().trim(),"http://mla-admin.sitsald.co.in/users/save_complain.json",confidential);
                // Log.d("ApiImageResponse", Response_image);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    progressDialog.dismiss();

                    Log.d("ApiImageResponse", " onPostExecute: " + Response_image);
                    if (!TextUtils.isEmpty(Response_image)) {
                        JSONObject jsonObject = new JSONObject(Response_image);
                        JSONObject jsonObject1=jsonObject.getJSONObject("message");
                        if (jsonObject1.getString("message").equals("Complaint Saved")) {
                            // FancyToast.makeText(NewMember.this, jsonObject1.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,  false).show();

                            if (user_id.equals("English")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("Your Complaint are Saved")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("आपकी सिकायत दर्ज कर ली गई है !")
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

                        }else
                            FancyToast.makeText(Confidantal.this, jsonObject.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
//video

    @SuppressLint("StaticFieldLeak")
    public void startVideo() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Confidantal.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                Response_image = VideoUploader.uploadImage(imageFileName,filePath,subareid,subarelistid,subcatid,
                        memberuid,com_detail.getText().toString().trim(),"http://mla-admin.sitsald.co.in/users/save_complain.json",confidential);
                // Log.d("ApiImageResponse", Response_image);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    progressDialog.dismiss();

                    Log.d("ApiImageResponse", " onPostExecute: " + Response_image);
                    if (!TextUtils.isEmpty(Response_image)) {
                        JSONObject jsonObject = new JSONObject(Response_image);
                        JSONObject jsonObject1=jsonObject.getJSONObject("message");
                        if (jsonObject1.getString("message").equals("Complaint Saved")) {
                            // FancyToast.makeText(NewMember.this, jsonObject1.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,  false).show();

                            if (user_id.equals("English")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("Your Complaint are Saved")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("आपकी सिकायत दर्ज कर ली गई है !")
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

                        }else
                            FancyToast.makeText(Confidantal.this, jsonObject.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    private void getsubcaDetails(final String id) {

        getSetSubcategoryLists.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Confidantal.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait  ...");
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/com_sub_category/"+id+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    getSetSubcategoryLists.clear();
                    if(user_id.equals("English")) {
                        getSetSubcategoryLists.add(new GetSetSubcategoryList(" ", "Select category","",""));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("ComSubCategory");

                            getSetSubcategoryLists.add(new GetSetSubcategoryList(packagedata.getString("id"), packagedata.getString("name_in_english"),packagedata.getString("video_req"),packagedata.getString("img_req")));
                        }
                    }else if(user_id.equals("Hindi")){
                        getSetSubcategoryLists.add(new GetSetSubcategoryList(" ", "केटेगरी चुने","",""));
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("ComSubCategory");
                            final String receivedText = StringEscapeUtils.unescapeJava(packagedata.getString("name_in_hindi"));

                            getSetSubcategoryLists.add(new GetSetSubcategoryList(packagedata.getString("id"), receivedText,packagedata.getString("video_req"),packagedata.getString("img_req")));
                        }
                    }
                    if (getSetSubcategoryLists.size() > 0) setsubcatData(getSetSubcategoryLists, spi_subcat);
                } catch (JSONException e) {
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
    private void setsubcatData(ArrayList<GetSetSubcategoryList> items, Spinner spinner) {
        ArrayAdapter<GetSetSubcategoryList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }
    private void setcatData(ArrayList<GetSetCategoryList> items, Spinner spinner) {
        ArrayAdapter<GetSetCategoryList> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

    }


    //
    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        } else enableCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) return;
        enableCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

           if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
             Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();
             filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
             videorecord.setVisibility(View.VISIBLE);

           }else if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE && data != null)
           {
                imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);

               videorecord1.setVisibility(View.VISIBLE);

           }
    }

    protected void enableCamera() {
        AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(activity, CAPTURE_MEDIA);
        videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
        videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_MEDIUM);
        //videoLimited.setVideoFileSize(15 * 1024 * 1024);
       // videoLimited.setMinimumVideoDuration(60000);
        videoLimited.setVideoDuration(30000);
        new Annca(videoLimited.build()).launchCamera();
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
        final ProgressDialog progressDialog = new ProgressDialog(Confidantal.this);
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
//call api



    @SuppressLint("StaticFieldLeak")
    public void startThread() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Confidantal.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                Response_image = Confi.uploadImage(imageFileName,filePath,subareid,subarelistid,subcatid,
                        memberuid,com_detail.getText().toString().trim(),"http://mla-admin.sitsald.co.in/users/save_complain.json",confidential);
//             Log.d("ApiImageResponse", Response_image);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    progressDialog.dismiss();

                   Log.d("ApiImageResponse", " onPostExecute: " + Response_image);
                    if (!TextUtils.isEmpty(Response_image)) {
                        JSONObject jsonObject = new JSONObject(Response_image);
                        JSONObject jsonObject1=jsonObject.getJSONObject("message");
                        if (jsonObject1.getString("message").equals("Complaint Saved")) {
                           // FancyToast.makeText(Confidantal.this, jsonObject1.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,  false).show();
                            if (user_id.equals("English")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("Your Complaint are Saved")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
                                builder.setMessage("आपकी सिकायत दर्ज कर ली गई है !")
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
                            FancyToast.makeText(Confidantal.this, jsonObject.getString("msg"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
//



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://mla-admin.sitsald.co.in/users/save_complain.json");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);
Log.d("checkmultipart", String.valueOf(sourceFile));
                // Adding file data to http body
                entity.addPart("com_video", new FileBody(sourceFile));
                entity.addPart("cat_id",
                        new StringBody(catid));
                entity.addPart("cat_sub_id",
                        new StringBody(subcatid));
                entity.addPart("user_id", new StringBody(memberuid));
                entity.addPart("com_text", new StringBody(com_detail.getText().toString().trim()));
                // Extra parameters if you want to pass to server
                entity.addPart("area_id",
                        new StringBody(subareid));
                entity.addPart("area_sub_id", new StringBody(subarelistid));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("response", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }
        private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
//
    //third mrthod
private void uploadVideo() {
    class UploadVideo extends AsyncTask<Void, Void, String> {

        ProgressDialog uploading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploading = ProgressDialog.show(Confidantal.this, "Uploading File", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploading.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(Confidantal.this);
            builder.setMessage(s).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Upload u = new Upload();
            @SuppressLint("WrongThread") String msg = u.uploadVideo(filePath,catid,subcatid,memberuid,com_detail.getText().toString().toLowerCase()
            ,subareid,subarelistid);
            return msg;
        }
    }
    UploadVideo uv = new UploadVideo();
    uv.execute();
}

    //otherway


}
