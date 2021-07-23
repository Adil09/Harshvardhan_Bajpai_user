package com.harshvardhanbajpai;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.harshvardhanbajpai.otherutilsfiles.Const;
import com.harshvardhanbajpai.otherutilsfiles.MyPefDatabase;
import com.harshvardhanbajpai.ui.prabharilist.PrabharilistActivity;
import com.harshvardhanbajpai.ui.votercardapply.VotercardApplyActivity;
import com.jaedongchicken.ytplayer.YoutubePlayerView;
import com.jaedongchicken.ytplayer.model.YTParams;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ArrayList<GetSetImageSilders> getSetImageSilders;
    ArrayList<GetSetImageSilders> getSetImageSilders1;
    ArrayList<NewsSilders>newsSilders=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
ImageView view_allphoto;
    private Context context;
    ViewPager viewPager,viewPager1;
    SpringDotsIndicator springDotsIndicator,springDotsIndicator1;
    Runnable runnable,runnable1;
    TextView tv;

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private Activity mActivity;
   TextView writen_msgdob,membership,toolbartitle,our_history,video_msg,writen_msg,confidetal_msg,
           complaint_history,question_msg,contact_history, prabharilist_tv,voter_tv;
   String user_id,user_eng;
   CardView memberrr_dop,historyy,questionss,memberrr,confi,compl,aboutharsh,cus,plist, voter_cv,prabharilist_cv;
   String memberuid;

   ImageView fbb,twii,logg,profileee,twitter,facebook,instabtn;
   String langchoose="";


    String currentVersion;

    // in app update
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        context = getApplicationContext();
        mActivity = Home.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");


        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        memberuid = s.getString("mla_id", "N/A");

        springDotsIndicator = findViewById(R.id.spring_dots_indicator);

        viewPager = findViewById(R.id.viewPager);
        voter_cv = findViewById(R.id.voter_cv);
        prabharilist_cv = findViewById(R.id.prabharilist_cv);
        voter_tv = findViewById(R.id.voter_tv);
        prabharilist_tv = findViewById(R.id.prabharilist_tv);

        springDotsIndicator1 = findViewById(R.id.spring_dots_indicator1);

        viewPager1 = findViewById(R.id.viewPager1);
        getSetImageSilders = new ArrayList<>();

        profileee=findViewById(R.id.profileee);


        profileee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                Log.d("checktoken",refreshedToken);
                startActivity(new Intent(getApplicationContext(),MyProfile.class));
            }
        });


        twii=findViewById(R.id.twii);


        twii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(getApplicationContext(),PhotoGallery.class));
            }
        });
        fbb=findViewById(R.id.fbb);


        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
alert_box();
            }
        });

        logg=findViewById(R.id.logg);
        logg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                builder.setCancelable(true);
                builder.setTitle("Logout");
                builder.setMessage("Are You Sure You Want to Logout !");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        MyPefDatabase.saveInOTPAuthenticate(context, "otp_status", "");

                        Intent o = new Intent(Home.this, Login.class);
                        o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences sssss=getSharedPreferences("MLA_id",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sssss.edit();
                        editor.clear().commit();
                        startActivity(o);

                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();

            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        setSliderImages1();
                                        setSliderImages();

                                        call_method(user_id);

                                        send_details1();
                                    }
                                }
        );
        final Handler h = new Handler();
        final int delay = 4000; /*1 second*/
        final int[] pagerIndex = {-1};
        h.postDelayed(new Runnable() {
            public void run() {
                pagerIndex[0]++;
                if (pagerIndex[0] >= getSetImageSilders.size()) pagerIndex[0] = 0;
                viewPager.setCurrentItem(pagerIndex[0]);
                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);


        final Handler h1 = new Handler();
        final int delay1 = 4000; /*1 second*/
        final int[] pagerIndex1 = {-1};
        h.postDelayed(new Runnable() {
            public void run() {
                pagerIndex1[0]++;
                if (pagerIndex1[0] >= newsSilders.size()) pagerIndex1[0] = 0;
                viewPager1.setCurrentItem(pagerIndex1[0]);
                runnable1 = this;
                h1.postDelayed(runnable1, delay1);
            }
        }, delay1);

        our_history=findViewById(R.id.our_history);
        video_msg=findViewById(R.id.video_msg);
        writen_msg=findViewById(R.id.writen_msg);
        confidetal_msg=findViewById(R.id.confidetal_msg);
        complaint_history=findViewById(R.id.complaint_history);
        toolbartitle=findViewById(R.id.toolbartitle);
        question_msg=findViewById(R.id.question_msg);
        membership=findViewById(R.id.membership);
        writen_msgdob=findViewById(R.id.writen_msgdob);
        contact_history=findViewById(R.id.contact_history);
        tv = (TextView) this.findViewById(R.id.mywidget);


        compl=findViewById(R.id.compl);
        compl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewMember.class));
            }
        });

        confi=findViewById(R.id.confi);
        confi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ConMessage.class));
            }
        });
        historyy=findViewById(R.id.historyy);
        historyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });
        aboutharsh=findViewById(R.id.aboutharsh);
        aboutharsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AboutHarsh.class));
            }
        });

        memberrr_dop=findViewById(R.id.memberrr_dop);
        memberrr_dop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferenc = getApplicationContext().getSharedPreferences("Call_id", MODE_PRIVATE);
                SharedPreferences.Editor edi = sharedPreferenc.edit();
                edi.putString("cn_id", "1").commit();
                startActivity(new Intent(getApplicationContext(),Development.class));
            }
        });

        plist=findViewById(R.id.plist);
        plist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ListProgram.class));
            }
        });
        cus=findViewById(R.id.cus);
        cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Contactus.class));
            }
        });
        questionss=findViewById(R.id.questionss);
        questionss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Questions.class));
            }
        });

        voter_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VotercardApplyActivity.class));
            }
        });
        prabharilist_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PrabharilistActivity.class));
            }
        });

        twitter = findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://twitter.com/Harshbajpai_MLA"));
                    intent.setPackage("com.twitter.android");
                    startActivity(intent);
                }
                catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/Harshbajpai_MLA")));
                }
            }
        });
        facebook = findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   String YourPageURL = "https://www.facebook.com/n/?Harsh.bajpai.3576";
                String YourPageURL = "https://www.facebook.com/harshbajpaiMLA";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

                startActivity(browserIntent);
            }
        });

        instabtn=findViewById(R.id.instabtn);
        instabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("http://instagram.com/_u/" + "hvbajpai"));
//                    intent.setPackage("com.instagram.android");
//                    startActivity(intent);
//                }
//                catch (android.content.ActivityNotFoundException anfe)
//                {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("https://www.instagram.com/hvbajpai")));
//                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.instagram.com/hvbajpai"));
                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                }
                catch (ActivityNotFoundException anfe)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/_u/" + "hvbajpai")));
                }

            }
        });
        memberrr=findViewById(R.id.memberrr);
        memberrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getactivedetails= MyPefDatabase.getFromDB(context,"membership_active");

                if (getactivedetails.equalsIgnoreCase("")){

                    if(user_id.equals("English")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setCancelable(true);
                        builder.setMessage("ARE YOU SURE YOU WANT TO JOIN MEMBERSHIP OF PROF. RAJENDRA KUMARI SMARAK SAMITI");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                send_details();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else if(user_id.equals("Hindi")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setCancelable(true);
                        builder.setMessage("क्या आप चाहते हैं कि आप प्रोफेसर राजेन्द्र कुमार बाजपेयी स्मारक समिति  के सदस्य बनें");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                send_details();
                            }
                        });
                        builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else {

                    if(user_id.equals("English")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setCancelable(true);
                        builder.setMessage("YOU ARE ALREADY A MEMBER OF PROF. RAJENDRA KUMARI SMARAK SAMITI, DO YOU WANT TO UNSUBSCRIBE?");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              //  send_details();
                                unsubscribemember();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else if(user_id.equals("Hindi")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setCancelable(true);
                        builder.setMessage("आप पहले से ही PROF के एक सदस्य हैं। रज़ेनद्र कुमारी SMARAK SAMITI, क्या आप UNSUBSCRIBE करना चाहते हैं?");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //send_details();
                                unsubscribemember();
                            }
                        });
                        builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }



            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");


                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                   // txtMessage.setText(message);
                }
            }
        };

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Home.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d("newToken",newToken);

            }
        });

        //check for membership
        checkmembership();

        //check for user status(banned or not)
        checkstatus();

        //in app update
        checkForAppUpdate();

        /*
        check for app update
        * */
        if (Const.CheckInternetConnection(context)){
            // checkfornewversion();
            new GetVersionCode().execute();
        }else {

        }
    }

    private void checkstatus() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(Home.this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/check_block_status.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1= jsonObject.getJSONObject("message");

                    Boolean error=jsonObject1.optBoolean("error");
                    String status=jsonObject1.optString("status");
                    if(!error){
                        //no action

                    }else {


                        //logout
                        if (!status.equalsIgnoreCase("data not found")){

                            try {
                                Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }

                            MyPefDatabase.saveInOTPAuthenticate(context, "otp_status", "");

                            Intent o = new Intent(Home.this, Login.class);
                            o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            SharedPreferences sssss=getSharedPreferences("MLA_id",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sssss.edit();
                            editor.clear().commit();
                            startActivity(o);
                        }


                    }




                } catch (Exception e) {
                    //e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                if (error.toString().equals("com.android.volley.AuthFailureError")) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                //  params.put("membership","0");
                params.put("id",memberuid);


                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    private void checkmembership() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(Home.this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/app_getmembership.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1= jsonObject.getJSONObject("message");

                    Boolean error=jsonObject1.getBoolean("error");
                    if(!error){


                        MyPefDatabase.saveInDB(context,"membership_active","Active");


                    }else {
                        MyPefDatabase.saveInDB(context,"membership_active","");
                        //FancyToast.makeText(getApplicationContext(),"All Field are required",FancyToast.LENGTH_LONG).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                if (error.toString().equals("com.android.volley.AuthFailureError")) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

              //  params.put("membership","0");
                params.put("id",memberuid);


                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }

    private void unsubscribemember() {
        final ProgressDialog progressDialog = new ProgressDialog(Home.this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mla-admin.sitsald.co.in/users/member_edit.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg=jsonObject.getString("message");
                    if(msg.equals("Saved")){


                        MyPefDatabase.saveInDB(context,"membership_active","");

                        if(user_id.equals("English")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setCancelable(true);
                            builder.setMessage("You have successfully unsubscribed Prof. Rajendra Kumari Bajpai Samiti. Thank you.");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else if(user_id.equals("Hindi")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setCancelable(true);
                            builder.setMessage("आपने राजेंद्र कुमारी बाजपेयी समिति को सफलतापूर्वक सदस्यता समाप्त कर दी है। धन्यवाद।");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("ओके ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }else {
                        //FancyToast.makeText(getApplicationContext(),"All Field are required",FancyToast.LENGTH_LONG).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
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

                params.put("membership","0");
                params.put("username",memberuid);


                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }
//news section

    private void setSliderImages1() {
        newsSilders.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_news.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                        JSONArray user = jsonObject.getJSONArray("news");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("News");
                            newsSilders.add(new NewsSilders(packagedata.getString("id"),  packagedata.getString("msg")));
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (newsSilders.size() > 0) {
                    viewPager1.setAdapter(new NewsAdapter(Home.this, newsSilders));
                    springDotsIndicator1.setViewPager(viewPager1);
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
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));


        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    protected void checkPermission () {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                mActivity, Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(
                mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                mActivity,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
            // Do something, when permissions are already granted
            // Toast.makeText(mContext, "Permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ) {
                    // Permissions are granted
                    // Toast.makeText(mContext, "Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
                    // Permissions are denied
                    //Toast.makeText(mContext, "Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }






    private void alert_box() {

        LayoutInflater li = LayoutInflater.from(Home.this);
        View promptsView = li.inflate(R.layout.activity_language_enquiry, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Home.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final RadioButton hindi = promptsView.findViewById(R.id.hindi);
      final RadioButton  eng=promptsView.findViewById(R.id.eng);
        if(user_id.equals("English")){
            eng.setChecked(true);
        }else {
            hindi.setChecked(true);
        }
        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                langchoose ="Hindi";

            }
        });
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                langchoose ="English";

            }
        });



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                if (langchoose.equals("")) {
dialog.dismiss();
                                } else {
                                    SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("User_id", MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    editor1.putString("user_id", langchoose).commit();
                                    Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @Override
    public void onRefresh() {
        setSliderImages1();
        setSliderImages();
        send_details1();
        call_method(user_id);

    }



    private void call_method(final String lan) {
        if(lan.equals("English")){
            question_msg.setText("SUGGESTIONS FOR VIDHAN SABHA QUESTION HOUR");
            our_history.setText("ABOUT HARSHVARDHAN");
            video_msg.setText("SEND COMPLAINT");
            writen_msg.setText("MEMBERSHIP OF PROF. RAJENDRA KUMARI BAJPAI SMARAK SAMITI");
            membership.setText("COMPLAINT/VIDHANSABHA QUESTION HISTORY");
            toolbartitle.setText("HARSH APP");
            confidetal_msg.setText("SEND CONFIDENTIAL MESSAGE");
            contact_history.setText("CONTACT US");
            complaint_history.setText("PROGRAM LIST ");
            writen_msgdob.setText("DEVELOPMENT WORK");
            voter_tv.setText("APPLY VOTER CARD");
            prabharilist_tv.setText("PRABHARI LIST");

            tv.setText("\uD83D\uDE4F\uD83D\uDE4F \n" +
                    "You are warmly welcome to this application - Harsh Vardhan Bajpai  \uD83D\uDE4F\uD83D\uDE4F");
            tv.setSelected(true);
          // send_eng();
        }else if(lan.equals("Hindi")) {
            Typeface font = Typeface.createFromAsset(getAssets(), "mangal.ttf");
            question_msg.setText("विधानसभा में प्रश्न पूंछे");
writen_msgdob.setText("विकास कार्य");
            voter_tv.setText("मतदाता कार्ड आवेदन");
            prabharilist_tv.setText("प्रभारी सूची");
            toolbartitle.setText("हर्षवर्धन");

            question_msg.setTypeface(font);
            toolbartitle.setTypeface(font);
           // send_hindi();
            our_history.setText("जीवन परिचय");
            contact_history.setText("सम्पर्क करे");
            video_msg.setText("शिकायत भेजे");
            writen_msg.setText("प्रोफेसर राजेन्द्र कुमार बाजपेयी स्मारक समिति के सदस्य बने");
            membership.setText("शिकायत / विधनसभा सवाल हिस्ट्री");
            confidetal_msg.setText("गोपनीय संदेस भेजे");
            complaint_history.setText("प्रोग्राम लिस्ट ");
            tv.setText("\uD83D\uDE4F\uD83D\uDE4F इस अप्लीकेशन  में आपका हार्दिक  स्वागत है - हर्ष वर्धन बाजपेयी  \uD83D\uDE4F\uD83D\uDE4F");
            our_history.setTypeface(font);
            contact_history.setTypeface(font);
            video_msg.setTypeface(font);
            writen_msg.setTypeface(font);
            confidetal_msg.setTypeface(font);
            membership.setTypeface(font);
            voter_tv.setTypeface(font);
            prabharilist_tv.setTypeface(font);
            complaint_history.setTypeface(font);
            tv.setTypeface(font);
            tv.setSelected(true);

        }
    }
//
private void send_details1() {

    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_view/"+memberuid+".json", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.d("GetURLReigration", "onResponse: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject jsonObject1=jsonObject.getJSONObject("message");

                JSONObject jsonObject2=jsonObject1.getJSONObject("$user");
                JSONObject jsonObject3=jsonObject2.getJSONObject("User");

                String photo=jsonObject3.getString("photo");


                if(photo.equals("null")){

                }else {
                    Glide.with(getApplicationContext()).load("http://mla-admin.sitsald.co.in/"+photo).into(profileee);
                    // Picasso.with(getApplicationContext()).load("http://mla-admin.sitsald.co.in/"+photo).into(prfilee);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (error.toString().equals("com.android.volley.AuthFailureError")) {

            }
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();

            return params;
        }
    };

    int sockettimeout = 5000;
    RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(retryPolicy);
    stringRequest.setShouldCache(false);
    AppController.getInstance().addToReqQueue(stringRequest);
}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this)

                    .setTitle("Exit App")
.setMessage("Are You Sure You Want to Exit !")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);

                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


        }

    }
    private void send_details() {
        final ProgressDialog progressDialog = new ProgressDialog(Home.this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mla-admin.sitsald.co.in/users/member_edit.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg=jsonObject.getString("message");
                    if(msg.equals("Saved")){


                        MyPefDatabase.saveInDB(context,"membership_active","active");

                        if(user_id.equals("English")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setCancelable(true);
                            builder.setMessage("You have successfully become a member of Prof. Rajendra Kumari Bajpai Samiti. You will be informed of all related programs and events. Thank you.");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else if(user_id.equals("Hindi")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setCancelable(true);
                            builder.setMessage("। धन");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("ओके ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }else {
                        //FancyToast.makeText(getApplicationContext(),"All Field are required",FancyToast.LENGTH_LONG).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
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

                params.put("membership","1");
                params.put("username",memberuid);


                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }







    //
    private void send_eng() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(true);
        builder.setMessage("");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void send_hindi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(true);
        builder.setMessage("");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
    private void setSliderImages() {
        getSetImageSilders.clear();
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/banners.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                swipeRefreshLayout.setRefreshing(false);
Log.d("checkres",response);
                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                        JSONArray user = jsonObject.getJSONArray("banner");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("Banner");
                            getSetImageSilders.add(new GetSetImageSilders(packagedata.getString("id"), "http://mla-admin.sitsald.co.in/" + packagedata.getString("image")));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getSetImageSilders.size() > 0) {
                    viewPager.setAdapter(new MyCustomPagerAdapter(Home.this, getSetImageSilders));
                    springDotsIndicator.setViewPager(viewPager);
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //in app update
        checkNewAppVersionState();

    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
                            @Override
                            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                //FLEXIBLE:
                                // If the update is downloaded but not installed,
                                // notify the user to complete the update.
                                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                    popupSnackbarForCompleteUpdateAndUnregister();
                                }

                                //IMMEDIATE:
                                if (appUpdateInfo.updateAvailability()
                                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                    // If an in-app update is already running, resume the update.
                                    startAppUpdateImmediate(appUpdateInfo);
                                }
                            }
                        });

    }

    //in app update
    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                }
            }
        });
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar =
                Snackbar.make(parentLayout, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.royalblue));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String latestVersion = "";
            currentVersion = getCurrentVersion();
            // Log.d(LOG_TAG, "Current version = " + currentVersion);
            try {


                Document doc = Jsoup.connect(Const.playstoreurl_withoutpackagename + context.getPackageName()).get();
                //latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

                //  Log.d(LOG_TAG, "Latest version = " + latestVersion);
            } catch (Exception e) {
                e.printStackTrace();
            }



            return latestVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            //If the versions are not the same
            if (!TextUtils.isEmpty(onlineVersion)){
                if(!currentVersion.equals(onlineVersion)){
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.AlertDialogDanger);
                    builder.setTitle("An Update is Available and Please Update and Rate our App");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Click button action
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                            } catch (Exception e) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Const.playstoreurl_withoutpackagename + context.getPackageName())));
                            }
                            dialog.dismiss();
                        }
                    });

           /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Cancel button action
                }
            });*/

                    builder.setCancelable(true);
                    builder.show();
                }
            }

        }
    }

    private String getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }
}
