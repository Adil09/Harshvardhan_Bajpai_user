package com.harshvardhanbajpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Complaint extends AppCompatActivity {
    ViewPager getViewPager;
    TabLayout getTabLayout;
    LinearLayout toolbar_title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        getViewPager = findViewById(R.id.viewpager);
        getTabLayout = findViewById(R.id.tablayout);

        getTabLayout.addTab(getTabLayout.newTab().setText("New Complaint"));
        getTabLayout.addTab(getTabLayout.newTab().setText("Previous Complaint Status"));
        final PagerAdapter1 adapter1 = new PagerAdapter1
                (getSupportFragmentManager(), getTabLayout.getTabCount());
        getViewPager.setAdapter(adapter1);
        getViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(getTabLayout));
        getTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }




}
