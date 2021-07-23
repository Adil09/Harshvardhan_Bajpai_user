package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Contactus extends AppCompatActivity {
String user_id;
LinearLayout toolbar_title_back;
TextView text;
Button directionn;
    ImageView fbb,twii,logg,twitter,facebook,instabtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        text = findViewById(R.id.text);
        call_method(user_id);
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

                //String YourPageURL = "https://www.facebook.com/n/?Harsh.bajpai.3576";
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
        directionn=findViewById(R.id.directionn);
        directionn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String url = "https://www.google.com/maps/search/?api=1&query="+"109, Rambagh, Prayagraj";
                String url = "https://www.google.com/maps/search/?api=1&query="+"Bajpai House, Rambagh, Prayagraj, Uttar Pradesh, 211003";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });

    }

    private void call_method(final String lan) {
        if (lan.equals("English")) {
            text.setText("CONTACT US");

            // send_eng();
        } else if (lan.equals("Hindi")) {
            // send_hindi();
            text.setText("सम्पर्क करे");


        }
    }
}