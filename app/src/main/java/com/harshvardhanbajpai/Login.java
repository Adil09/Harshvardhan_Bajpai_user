package com.harshvardhanbajpai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
Button bottom_pagelayout;
Button get_start;
RadioButton hindi,eng;
RadioGroup radio_group;
String lang="";
ImageView profile_image;
EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        get_start=findViewById(R.id.button);

        bottom_pagelayout=findViewById(R.id.bottom_pagelayout);
        bottom_pagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        radio_group=findViewById(R.id.radio_group);
        profile_image=findViewById(R.id.profile_image);
        email=findViewById(R.id.email);
hindi=findViewById(R.id.hindi);
eng=findViewById(R.id.eng);

        get_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!lang.equals("")){
                            send_details();
                        }else {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                            builder.setCancelable(true);
                            builder.setMessage("Select Language");
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
                    }
                });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang="Hindi";

            }
        });
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang="English";

            }
        });


    }
    static class MobileNumberValidation {
        public static boolean isValid(String s) {
            Pattern p = Pattern.compile("[6-9][0-9]{9}");
            Matcher m = p.matcher(s);
            return (m.find() && m.group().equals(s));
        }
    }
    private void call_activity(String hindi) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User_id", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id",hindi).commit();

        startActivity(new Intent(getApplicationContext(),Home.class));
    }

    private void send_details() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/app_login.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("GetURLReigration", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1=jsonObject.getJSONObject("message");
                        boolean error=jsonObject1.optBoolean("error");
                        String msge=jsonObject1.optString("msge");

                        if(!error){
                            JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                            JSONObject jsonObject3=jsonObject2.getJSONObject("User");
                            String username=jsonObject3.getString("username");
                            String otp=jsonObject1.getString("otp");

                            try {
                                Toast.makeText(Login.this, msge, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }

                            SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("User_id", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putString("user_id", lang).commit();
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MLA_id", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("mla_id",username).commit();
                            Intent intent = new Intent(getApplicationContext(), OTPPage.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("otp",otp);
                            startActivity(intent);
                        }else {
                            try {
                                Toast.makeText(Login.this, msge, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }

                        }


                } catch (Exception e) {
                 //   e.printStackTrace();
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

                params.put("username",email.getText().toString().trim());
                params.put("push_device_token",refreshedToken);


                return params;
            }
        };

        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToReqQueue(stringRequest);
    }
}
