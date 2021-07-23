package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class History extends AppCompatActivity {
String user_id;
LinearLayout toolbar_title_back;
TextView text,writen_msg,confidetal_msg,confidetal_quest;
CardView memberrr,confi,hist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        text=findViewById(R.id.text);
        writen_msg=findViewById(R.id.writen_msg);
        confidetal_msg=findViewById(R.id.confidetal_msg);
        confidetal_quest=findViewById(R.id.confidetal_quest);

        hist=findViewById(R.id.hist);
        hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QUESTIONComplainHistory.class));
            }
        });
        confi=findViewById(R.id.confi);
        confi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ConMessage.class));
            }
        });
        memberrr=findViewById(R.id.memberrr);
        memberrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ComplainHistory.class));
            }
        });

        call_method(user_id);
    }
    private void call_method(final String lan) {
        if(lan.equals("English")){

            text.setText("COMPLAINT HISTORY");

            writen_msg.setText("NORMAL COMPLAINT HISTORY");
            confidetal_msg.setText("CONFIDENTIAL COMPLAINT HISTORY");
            confidetal_quest.setText("SUGGESTIONS FOR VIDHAN SABHA QUESTION HOUR HISTORY");
        }else if(lan.equals("Hindi")) {
            Typeface font = Typeface.createFromAsset(getAssets(), "mangal.ttf");
            text.setText("शिकायत हिस्ट्री");
            writen_msg.setText("नोरमल शिकायत हिस्ट्री");
            confidetal_msg.setText("कॉन्फिडेंटिअल शिकायत हिस्ट्री");
            confidetal_quest.setText("विधान सभा सवाल ऑवर हिस्ट्री");
            text.setTypeface(font);
            writen_msg.setTypeface(font);
            confidetal_msg.setTypeface(font);
            confidetal_quest.setTypeface(font);

        }
    }
}
