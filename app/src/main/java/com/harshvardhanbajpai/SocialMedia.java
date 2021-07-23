package com.harshvardhanbajpai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;

public class SocialMedia extends AppCompatActivity {
    ArrayList<GetSetImageSilders> getSetImageSilders;
LinearLayout toolbar_title_back;
    ViewPager viewPager;
    SpringDotsIndicator springDotsIndicator;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        springDotsIndicator = findViewById(R.id.spring_dots_indicator);

        viewPager = findViewById(R.id.viewPager);
        getSetImageSilders = new ArrayList<>();
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


    }

}
