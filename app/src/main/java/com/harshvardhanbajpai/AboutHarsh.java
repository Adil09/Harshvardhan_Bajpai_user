package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutHarsh extends AppCompatActivity {
    LinearLayout toolbar_title_back;
TextView text;
String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_harsh);

        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        text=findViewById(R.id.text);
        call_method(user_id);
    }

    private void call_method(final String lan) {
        if (lan.equals("English")) {
            text.setText("ABOUT HARSHVARDHAN");

            // send_eng();
        } else if (lan.equals("Hindi")) {
            // send_hindi();
            text.setText("जीवन परिचय");


        }
    }
}
