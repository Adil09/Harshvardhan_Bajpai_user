package com.harshvardhanbajpai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.harshvardhanbajpai.otherutilsfiles.MyPefDatabase;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import androidx.appcompat.app.AppCompatActivity;

public class OTPPage extends AppCompatActivity {
Button button_verify;
    private SmsVerifyCatcher smsVerifyCatcher;
     EditText pinView;
    String val = "";
     TextView textView_noti;
     String OTP;
     Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        context= OTPPage.this;
Bundle bundle=getIntent().getExtras();

if(bundle!= null){
    OTP=bundle.getString("otp");
}
        pinView=findViewById(R.id.pinView);
        textView_noti=findViewById(R.id.textView_noti);
        button_verify=findViewById(R.id.button_verify);
        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinView.getText().toString().equals(OTP)){
                    MyPefDatabase.saveInOTPAuthenticate(context, "otp_status", "verified");
                    Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    MyPefDatabase.saveInOTPAuthenticate(context, "otp_status", "");
                    Toast.makeText(getApplicationContext(),"OTP INVALID", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
