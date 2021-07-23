package com.harshvardhanbajpai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.harshvardhanbajpai.adapter.Area_adapter;
import com.harshvardhanbajpai.adapter.Financialyearfilter_adapter;
import com.harshvardhanbajpai.adapter.Nidhi_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Development extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
String user_id;
TextView text;
LinearLayout toolbar_title_back;
    Context context;
    String memberuid;
EditText editTextSearch;
    ArrayList<DevelopmentModel>my_order_recycler=new ArrayList<>();
    RecyclerView comp;
PackageRecycler packageRecycler;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView nocom;
    LinearLayout filterrr;
    String year_id,call_id,nidhi_id,area_id;

    LinearLayout financialyear_lay, nidhi_lay,area_lay, filterrootlay;
    Boolean bfinancialyear= false, barea=false, bnidhi=false;
    TextView area_tv,nidhi_tv,financial_tv, clearfilter_tv,done_tv;
    ImageView image_arrow, aimage_arrow, simage_arrow;
    RecyclerView rv_listfilter;
    ArrayList<FilterYear>filterYears=new ArrayList<>();
    ArrayList<FilterArea>filterArea=new ArrayList<>();
    ArrayList<FilterNidhi>filterNidhi=new ArrayList<>();
    Financialyearfilter_adapter financialyearfilter_adapter ;
    Area_adapter actiionList_adapter;
    Nidhi_adapter statusList_adapter;
    String filteryearsstr="", filterareastr="", filternidhistr="",selected_str ="";
    LinearLayout filterkeywords_lay;
    TextView keyword_tv;
    ArrayList<String> selectdkeylist= new ArrayList<>();
    //String selected_str ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development);
        context= Development.this;
       // Toast.makeText(context, "working", Toast.LENGTH_SHORT).show();
        SharedPreferences s = getSharedPreferences("MLA_id", MODE_PRIVATE);
        memberuid = s.getString("mla_id", "N/A");
        SharedPreferences ss = getSharedPreferences("User_id", MODE_PRIVATE);
        user_id = ss.getString("user_id", "N/A");
        SharedPreferences sss = getSharedPreferences("Year_id", MODE_PRIVATE);
        year_id = sss.getString("year_id", "N/A");

        SharedPreferences ssss = getSharedPreferences("Area_id", MODE_PRIVATE);
        area_id = ssss.getString("area_id", "N/A");
        SharedPreferences sssss = getSharedPreferences("Nidhi_id", MODE_PRIVATE);
        nidhi_id = sssss.getString("n_id", "N/A");

        SharedPreferences ssssss = getSharedPreferences("Call_id", MODE_PRIVATE);
        call_id = ssssss.getString("cn_id", "N/A");

        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),Home.class));
                SharedPreferences sssss2=getSharedPreferences("Nidhi_id",MODE_PRIVATE);

                SharedPreferences.Editor editor2=sssss2.edit();

                editor2.clear().commit();

                SharedPreferences sssss1=getSharedPreferences("Year_id",MODE_PRIVATE);

                SharedPreferences.Editor editor1=sssss1.edit();

                editor1.clear().commit();

                SharedPreferences sssss=getSharedPreferences("Area_id",MODE_PRIVATE);

                SharedPreferences.Editor editor=sssss.edit();

                editor.clear().commit();



                SharedPreferences sssss3=getSharedPreferences("Call_id",MODE_PRIVATE);

                SharedPreferences.Editor editor3=sssss3.edit();

                editor3.clear().commit();
                finish();
            }
        });
        comp=findViewById(R.id.comp);
        financialyear_lay=findViewById(R.id.financialyear_lay);
        keyword_tv=findViewById(R.id.keyword_tv);
        filterkeywords_lay=findViewById(R.id.filterkeywords_lay);
        area_lay=findViewById(R.id.area_lay);
        filterrootlay=findViewById(R.id.filterrootlay);
        nidhi_lay=findViewById(R.id.nidhi_lay);
        text=findViewById(R.id.text);
        nocom=findViewById(R.id.nocom);
        simage_arrow= findViewById(R.id.simage_arrow);
        aimage_arrow= findViewById(R.id.aimage_arrow);
        image_arrow= findViewById(R.id.image_arrow);
        nidhi_tv=findViewById(R.id.nidhi_tv);
        done_tv= findViewById(R.id.done_tv);
        clearfilter_tv= findViewById(R.id.clearfilter_tv);
        rv_listfilter= findViewById(R.id.rv_listfilter);
        financial_tv=findViewById(R.id.financial_tv);
        area_tv=findViewById(R.id.area_tv);
        editTextSearch=findViewById(R.id.editTextSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout1);
        swipeRefreshLayout.setOnRefreshListener(this);

        if(call_id.equals("2")){
            package_ser();
            call_method(user_id);
        }else {

            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);

                                            package_list();


                                            call_method(user_id);


                                        }
                                    }
            );
        }
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        rv_listfilter.setLayoutManager(layoutManager);

     //   rv_listfilter.setAdapter(new Area_adapter(context, filterArea));
        actiionList_adapter = new Area_adapter(context, R.layout.item_year, filterArea);
        actiionList_adapter.notifyDataSetChanged();

      //  rv_listfilter.setAdapter(new Financialyearfilter_adapter(context, filterYears));
        financialyearfilter_adapter = new Financialyearfilter_adapter(context, R.layout.item_year, filterYears);
        financialyearfilter_adapter.notifyDataSetChanged();

       // rv_listfilter.setAdapter(new Nidhi_adapter(context, filterNidhi));
        statusList_adapter = new Nidhi_adapter(context, R.layout.item_year, filterNidhi);
        statusList_adapter.notifyDataSetChanged();

        filterrr=findViewById(R.id.filterrr);
        filterrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Filter_id", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("f_id", year_id).commit();
                editor1.putString("a_id", area_id).commit();
                editor1.putString("n_id", nidhi_id).commit();



                Intent i = new Intent(getApplicationContext(),FilterActivity.class);

//                SharedPreferences sssss2=getSharedPreferences("Nidhi_id",MODE_PRIVATE);
//
//                SharedPreferences.Editor editor2=sssss2.edit();
//
//                editor2.clear().commit();
//
//                SharedPreferences sssss1=getSharedPreferences("Year_id",MODE_PRIVATE);
//
//                SharedPreferences.Editor editor1=sssss1.edit();
//
//                editor1.clear().commit();
//
//                SharedPreferences sssss=getSharedPreferences("Area_id",MODE_PRIVATE);
//
//                SharedPreferences.Editor editor=sssss.edit();
//
//                editor.clear().commit();

                startActivity(i);

            }
        });


        financialyear_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bfinancialyear){
                    bfinancialyear=false;
                    barea=false;
                    bnidhi=false;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    financial_tv.setTypeface(null, Typeface.NORMAL);
                    area_tv.setTypeface(null, Typeface.NORMAL);
                    nidhi_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.downarrow);
                    aimage_arrow.setImageResource(R.drawable.downarrow);
                    simage_arrow.setImageResource(R.drawable.downarrow);
                    filterrootlay.setVisibility(View.GONE);

                }else {
                    bfinancialyear= true;
                    barea=false;
                    bnidhi=false;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.selectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    financial_tv.setTypeface(null, Typeface.BOLD);
                    area_tv.setTypeface(null, Typeface.NORMAL);
                    nidhi_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.uparrow);
                    aimage_arrow.setImageResource(R.drawable.downarrow);
                    simage_arrow.setImageResource(R.drawable.downarrow);
                    filterrootlay.setVisibility(View.VISIBLE);
                    rv_listfilter.setVisibility(View.VISIBLE);
                    rv_listfilter.setAdapter(new Financialyearfilter_adapter(context, filterYears));
                    financialyearfilter_adapter = new Financialyearfilter_adapter(context, R.layout.item_year, filterYears);
                    financialyearfilter_adapter.notifyDataSetChanged();
                }


            }
        });

        area_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(barea){
                    bfinancialyear=false;
                    barea=false;
                    bnidhi=false;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    financial_tv.setTypeface(null, Typeface.NORMAL);
                    area_tv.setTypeface(null, Typeface.NORMAL);
                    nidhi_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.downarrow);
                    aimage_arrow.setImageResource(R.drawable.downarrow);
                    simage_arrow.setImageResource(R.drawable.downarrow);
                    filterrootlay.setVisibility(View.GONE);

                }else {
                    bfinancialyear=false;
                    barea=true;
                    bnidhi=false;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.selectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_tv.setTypeface(null, Typeface.BOLD);
                    financial_tv.setTypeface(null, Typeface.NORMAL);
                    nidhi_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.downarrow);
                    aimage_arrow.setImageResource(R.drawable.uparrow);
                    simage_arrow.setImageResource(R.drawable.downarrow);
                    filterrootlay.setVisibility(View.VISIBLE);
                    rv_listfilter.setVisibility(View.VISIBLE);
                    rv_listfilter.setAdapter(new Area_adapter(context, filterArea));
                    actiionList_adapter = new Area_adapter(context, R.layout.item_year, filterArea);
                    actiionList_adapter.notifyDataSetChanged();
                }
            }
        });

        nidhi_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bnidhi){
                    bfinancialyear=false;
                    barea=false;
                    bnidhi=false;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    financial_tv.setTypeface(null, Typeface.NORMAL);
                    area_tv.setTypeface(null, Typeface.NORMAL);
                    nidhi_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.downarrow);
                    aimage_arrow.setImageResource(R.drawable.downarrow);
                    simage_arrow.setImageResource(R.drawable.downarrow);
                    filterrootlay.setVisibility(View.GONE);

                }else {
                    bfinancialyear=false;
                    barea=false;
                    bnidhi=true;
                    financialyear_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    area_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.unselectedfilter));
                    nidhi_lay.setBackground(ContextCompat.getDrawable(context, R.drawable.selectedfilter));
                    nidhi_tv.setTypeface(null, Typeface.BOLD);
                    financial_tv.setTypeface(null, Typeface.NORMAL);
                    area_tv.setTypeface(null, Typeface.NORMAL);
                    image_arrow.setImageResource(R.drawable.downarrow);
                    aimage_arrow.setImageResource(R.drawable.downarrow);
                    simage_arrow.setImageResource(R.drawable.uparrow);
                    filterrootlay.setVisibility(View.VISIBLE);
                    rv_listfilter.setVisibility(View.VISIBLE);
                    rv_listfilter.setAdapter(new Nidhi_adapter(context, filterNidhi));
                    statusList_adapter = new Nidhi_adapter(context, R.layout.item_year, filterNidhi);
                    statusList_adapter.notifyDataSetChanged();

                }
            }
        });

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bfinancialyear){
                    filterrootlay.setVisibility(View.GONE);
                    String formattedStringcategorys="";
                    //categoriesList_adapter.getlist().size();
                    for (int i=0; i  < filterYears.size(); i++){

                        if (filterYears.get(i).isIs_select()){

                            if (formattedStringcategorys.length() > 0)
                                formattedStringcategorys +=",";

                            formattedStringcategorys += filterYears.get(i).getYear();
                        }


                    }

                    getareastr();
                    getnidhistr();
                   // getyearstr();

                   // filterpaginatio=1;
                    filteryearsstr=formattedStringcategorys;
                    // checllastitem= "sometext";
                    filterfunctio(formattedStringcategorys, filterareastr, filternidhistr);
                   // filterfunctio(filteryearsstr, filterareastr, filternidhistr);

                }else if (barea){
                    filterrootlay.setVisibility(View.GONE);
                    String formattedStringcategorys="";
                    //categoriesList_adapter.getlist().size();
                    for (int i=0; i  < filterArea.size(); i++){

                        if (filterArea.get(i).isIs_select()){

                            if (formattedStringcategorys.length() > 0)
                                formattedStringcategorys +=",";

                            formattedStringcategorys += filterArea.get(i).getYear();
                        }

                    }

                  //  getareastr();
                    getnidhistr();
                    getyearstr();

                  //  filterpaginatio=1;
                    filterareastr=formattedStringcategorys;
                    // checllastitem= "sometext";
                   filterfunctio(filteryearsstr, formattedStringcategorys, filternidhistr);
                 //   filterfunctio(filteryearsstr, filterareastr, filternidhistr);

                }else if (bnidhi){
                    filterrootlay.setVisibility(View.GONE);
                    String formattedStringcategorys="";
                    //categoriesList_adapter.getlist().size();
                    for (int i=0; i  < filterNidhi.size(); i++){

                        if (filterNidhi.get(i).isIs_select()){

                            if (formattedStringcategorys.length() > 0)
                                formattedStringcategorys +=",";

                            formattedStringcategorys += filterNidhi.get(i).getYear();
                        }

                    }

                    getareastr();
                  //  getnidhistr();
                    getyearstr();

                  //  filterpaginatio=1;
                    filternidhistr=formattedStringcategorys;

                    //  checllastitem= "sometext";
                    filterfunctio(filteryearsstr, filterareastr, formattedStringcategorys);
                    //filterfunctio(filteryearsstr, filterareastr, filternidhistr);
                }

                //show selected keywords
                selectdkeylist.clear();
                selected_str="";
                String[] namesListyear = filteryearsstr.split(",");
                String[] namesListyarea = filterareastr.split(",");
                String[] namesListynidhi = filternidhistr.split(",");

                for(String name : namesListyear){
                    if (!name.equalsIgnoreCase(""))
                    selectdkeylist.add(name);
                } for(String name : namesListyarea){
                    if (!name.equalsIgnoreCase(""))
                    selectdkeylist.add(name);
                } for(String name : namesListynidhi){
                    if (!name.equalsIgnoreCase(""))
                    selectdkeylist.add(name);
                }

                if (selectdkeylist.size() > 0){
                    filterkeywords_lay.setVisibility(View.VISIBLE);
                    for (int i=0; i < selectdkeylist.size(); i++){

                        if (selected_str.length() > 0)
                            selected_str= selected_str+","+selectdkeylist.get(i);
                        else selected_str= selectdkeylist.get(i);
                    }
                    keyword_tv.setText(selected_str);
                }else {
                    filterkeywords_lay.setVisibility(View.GONE);
                }



                //ArrayList<String> elephantList = (ArrayList<String>) Arrays.asList(filteryearsstr.split(","));
            }

        });

        clearfilter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bfinancialyear){
                    for (int i=0; i  < filterYears.size(); i++){

                        if (filterYears.get(i).isIs_select()){

                            filterYears.get(i).setIs_select(false);
                        }
                        financialyearfilter_adapter.notifyDataSetChanged();

                    }

                    cleararea();
                    clearnidhi();
                 //   clearyear();

                    rv_listfilter.setAdapter(new Financialyearfilter_adapter(context, filterYears));
                    financialyearfilter_adapter = new Financialyearfilter_adapter(context, R.layout.item_year, filterYears);
                    financialyearfilter_adapter.notifyDataSetChanged();

                }else if (barea){
                    for (int i=0; i  < filterArea.size(); i++){

                        if (filterArea.get(i).isIs_select()){

                            filterArea.get(i).setIs_select(false);
                        }
                        actiionList_adapter.notifyDataSetChanged();

                    }

                   // cleararea();
                    clearnidhi();
                    clearyear();

                    rv_listfilter.setAdapter(new Area_adapter(context, filterArea));
                    actiionList_adapter = new Area_adapter(context, R.layout.item_year, filterArea);
                    actiionList_adapter.notifyDataSetChanged();

                }else if (bnidhi){

                    for (int i=0; i  < filterNidhi.size(); i++){

                        if (filterNidhi.get(i).isIs_select()){

                            filterNidhi.get(i).setIs_select(false);
                        }
                        statusList_adapter.notifyDataSetChanged();

                    }

                    cleararea();
                    //clearnidhi();
                    clearyear();

                    rv_listfilter.setAdapter(new Nidhi_adapter(context, filterNidhi));
                    statusList_adapter = new Nidhi_adapter(context, R.layout.item_year, filterNidhi);
                    statusList_adapter.notifyDataSetChanged();
                }

                //Hide selected keywords
                selected_str="";
                selectdkeylist.clear();
                filterkeywords_lay.setVisibility(View.GONE);
                keyword_tv.setText("");

            }
        });
        
        //load filter data
        package_financialyear();
        package_area();
        package_nidhi();

    }

    private void clearyear() {

        for (int i=0; i  < filterYears.size(); i++){

            if (filterYears.get(i).isIs_select()){

                filterYears.get(i).setIs_select(false);
            }
            financialyearfilter_adapter.notifyDataSetChanged();

        }

        rv_listfilter.setAdapter(new Financialyearfilter_adapter(context, filterYears));
        financialyearfilter_adapter = new Financialyearfilter_adapter(context, R.layout.item_year, filterYears);
        financialyearfilter_adapter.notifyDataSetChanged();

    }

    private void clearnidhi() {

        for (int i=0; i  < filterNidhi.size(); i++){

            if (filterNidhi.get(i).isIs_select()){

                filterNidhi.get(i).setIs_select(false);
            }
            statusList_adapter.notifyDataSetChanged();

        }
        rv_listfilter.setAdapter(new Nidhi_adapter(context, filterNidhi));
        statusList_adapter = new Nidhi_adapter(context, R.layout.item_year, filterNidhi);
        statusList_adapter.notifyDataSetChanged();

    }

    private void cleararea() {

        for (int i=0; i  < filterArea.size(); i++){

            if (filterArea.get(i).isIs_select()){

                filterArea.get(i).setIs_select(false);
            }
            actiionList_adapter.notifyDataSetChanged();

        }

        rv_listfilter.setAdapter(new Area_adapter(context, filterArea));
        actiionList_adapter = new Area_adapter(context, R.layout.item_year, filterArea);
        actiionList_adapter.notifyDataSetChanged();

    }

    private void getyearstr() {

        String formattedStringcategorys="";
        //categoriesList_adapter.getlist().size();
        for (int i=0; i  < filterYears.size(); i++){

            if (filterYears.get(i).isIs_select()){

                if (formattedStringcategorys.length() > 0)
                    formattedStringcategorys +=",";

                formattedStringcategorys += filterYears.get(i).getYear();
            }


        }
        // filterpaginatio=1;
        filteryearsstr=formattedStringcategorys;
    }

    private void getnidhistr() {

        String formattedStringcategorys="";
        //categoriesList_adapter.getlist().size();
        for (int i=0; i  < filterNidhi.size(); i++){

            if (filterNidhi.get(i).isIs_select()){

                if (formattedStringcategorys.length() > 0)
                    formattedStringcategorys +=",";

                formattedStringcategorys += filterNidhi.get(i).getYear();
            }

        }

        //  filterpaginatio=1;
        filternidhistr=formattedStringcategorys;

    }

    private void getareastr() {

        String formattedStringcategorys="";
        //categoriesList_adapter.getlist().size();
        for (int i=0; i  < filterArea.size(); i++){

            if (filterArea.get(i).isIs_select()){

                if (formattedStringcategorys.length() > 0)
                    formattedStringcategorys +=",";

                formattedStringcategorys += filterArea.get(i).getYear();
            }

        }

        //  filterpaginatio=1;
        filterareastr=formattedStringcategorys;
    }

    private void filterfunctio(final String years, final String area, final String nidhi) {

        my_order_recycler.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Development.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/app_search.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkresssss",response);
                progressDialog.dismiss();

                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("user");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("development_works");


                        if (user_id.equals("English")) {

                            my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                    packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                    packagedata.getString("status"),

                                    packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));

                        } else if (user_id.equals("Hindi")) {

                            my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                    packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                    packagedata.getString("status"),

                                    packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));}

                    }

                    if (my_order_recycler.size() > 0) {

                        packageRecycler = new PackageRecycler(getApplicationContext(), R.layout.development_recycler, my_order_recycler);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Development.this, 1);
                        comp.setLayoutManager(mLayoutManager);
                        comp.setAdapter(packageRecycler);
                        comp.getRecycledViewPool().clear();
                        packageRecycler.notifyItemRangeChanged(0, my_order_recycler.size());

                        //
//


                    }

                } catch (Exception e) {
                    e.printStackTrace();
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

                /*if(year_id.equals("n")){
                    hashMap.put("f_year","KT");
                }else {
                    hashMap.put("f_year",year_id);
                }
                if(nidhi_id.equals("n")){
                    hashMap.put("f_nidhi","KT");
                }else {
                    hashMap.put("f_nidhi",nidhi_id);
                }
                if(area_id.equals("n")){
                    hashMap.put("f_area","KT");
                }else {
                    hashMap.put("f_area",area_id);
                }*/

                if (years.trim().equalsIgnoreCase("")){
                    hashMap.put("f_year","KT");
                }else {
                    hashMap.put("f_year",years);
                }
                if (nidhi.trim().equalsIgnoreCase("")){
                    hashMap.put("f_nidhi","KT");
                }else {
                    hashMap.put("f_nidhi",nidhi);
                }
                if (area.trim().equalsIgnoreCase("")){
                    hashMap.put("f_area","KT");
                }else {
                    hashMap.put("f_area",area);
                }

                return hashMap;
            }
        };
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void package_nidhi() {

        filterNidhi.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_nidhi.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("development_works");


                        filterNidhi.add(new FilterNidhi(packagedata.getString("nidhi")
                        ));


                    }



                  /*  if (filterNidhi.size() > 0) {

                        RecyclerviewAddressAdapter2 packageRecycler = new RecyclerviewAddressAdapter2(getApplicationContext(),filterNidhi);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                        nidhi_recycler.setLayoutManager(mLayoutManager);
                        nidhi_recycler.setAdapter(packageRecycler);
                        nidhi_recycler.getRecycledViewPool().clear();




                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
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

    private void package_area() {

        filterArea.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_saerch_area.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkarea",response);

                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("development_works");


                        filterArea.add(new FilterArea(packagedata.getString("area")
                        ));
                    }


/*
                    if (filterArea.size() > 0) {

                        RecyclerviewAddressAdapterArea packageRecycler = new RecyclerviewAddressAdapterArea(getApplicationContext(),filterArea);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                        area_recycler.setLayoutManager(mLayoutManager);
                        area_recycler.setAdapter(packageRecycler);




                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
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

    private void package_financialyear() {

        filterYears.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_financial_year.json",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("area");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("development_works");



                        filterYears.add(new FilterYear(packagedata.getString("financial_year")
                        ));


                    }


/*
                    if (filterYears.size() > 0) {

                        RecyclerviewAddressAdapter packageRecycler = new RecyclerviewAddressAdapter(getApplicationContext(),filterYears);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                        year_recycler.setLayoutManager(mLayoutManager);
                        year_recycler.setAdapter(packageRecycler);
                        year_recycler.getRecycledViewPool().clear();




                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Home.class));
        SharedPreferences sssss2=getSharedPreferences("Nidhi_id",MODE_PRIVATE);

        SharedPreferences.Editor editor2=sssss2.edit();

        editor2.clear().commit();

        SharedPreferences sssss1=getSharedPreferences("Year_id",MODE_PRIVATE);

        SharedPreferences.Editor editor1=sssss1.edit();

        editor1.clear().commit();

        SharedPreferences sssss=getSharedPreferences("Area_id",MODE_PRIVATE);

        SharedPreferences.Editor editor=sssss.edit();

        editor.clear().commit();
        finish();
    }

    private void filter(String text) {
        text=text.toLowerCase();
        //new array list that will hold the filtered data
        ArrayList<DevelopmentModel>cart=new ArrayList<>();
        for(DevelopmentModel getSetCategory:my_order_recycler)
        {
            String name=getSetCategory.getArea().toLowerCase();
            String year=getSetCategory.getFinancial_year().toLowerCase();
            String nidhi=getSetCategory.getNidhi().toLowerCase();
            String subject=getSetCategory.getSubject().toLowerCase();

            if(name.contains(text)){
                cart.add(getSetCategory);
            }else if(year.contains(text)){
                cart.add(getSetCategory);
            }else if(nidhi.contains(text)){
                cart.add(getSetCategory);
            }else if(subject.contains(text)){
                cart.add(getSetCategory);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        packageRecycler.setFilter(cart);


    }
    @Override
    public void onRefresh() {

        package_list();


        call_method(user_id);

    }
    private void call_method(final String lan) {
        if (lan.equals("English")) {

            text.setText("DEVELOPMENT");

        } else if (lan.equals("Hindi")) {
            Typeface font = Typeface.createFromAsset(getAssets(), "mangal.ttf");
            text.setText("विकास");

            text.setTypeface(font);


        }
    }
    private void package_ser() {

        my_order_recycler.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Development.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://mla-admin.sitsald.co.in/users/app_search.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
Log.d("checkresssss",response);
                progressDialog.dismiss();

                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                    JSONArray user = jsonObject.getJSONArray("user");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject loopdata = user.getJSONObject(i);
                        JSONObject packagedata = loopdata.getJSONObject("development_works");


                        if (user_id.equals("English")) {

                            my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                    packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                    packagedata.getString("status"),

                                    packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));

                        } else if (user_id.equals("Hindi")) {

                            my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                    packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                    packagedata.getString("status"),

                                    packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));}

                    }

                    if (my_order_recycler.size() > 0) {

                        packageRecycler = new PackageRecycler(getApplicationContext(), R.layout.development_recycler, my_order_recycler);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Development.this, 1);
                        comp.setLayoutManager(mLayoutManager);
                        comp.setAdapter(packageRecycler);
                        comp.getRecycledViewPool().clear();
                        packageRecycler.notifyItemRangeChanged(0, my_order_recycler.size());

                        //
//


                    }

                } catch (Exception e) {
                    e.printStackTrace();
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

                if(year_id.equals("n")){
                    hashMap.put("f_year","KT");
                }else {
                    hashMap.put("f_year",year_id);
                }
                if(nidhi_id.equals("n")){
                    hashMap.put("f_nidhi","KT");
                }else {
                    hashMap.put("f_nidhi",nidhi_id);
                }
                if(area_id.equals("n")){
                    hashMap.put("f_area","KT");
                }else {
                    hashMap.put("f_area",area_id);
                }

                return hashMap;
            }
        };
        int sockettimeout = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void package_list() {
        swipeRefreshLayout.setRefreshing(true);
        my_order_recycler.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Development.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_works.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();

                try {

                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject = jd.getJSONObject("message");

                        JSONArray user = jsonObject.getJSONArray("area");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("development_works");


                            if (user_id.equals("English")) {

                                my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                        packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                        packagedata.getString("status"),

                                        packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));

                            } else if (user_id.equals("Hindi")) {

                                my_order_recycler.add(new DevelopmentModel(packagedata.getString("id"), packagedata.getString("financial_year"),
                                        packagedata.getString("area"), packagedata.getString("work_description"), packagedata.getString("nidhi"),
                                        packagedata.getString("status"),

                                        packagedata.getString("image"),packagedata.getString("estimate"),packagedata.getString("subject")));}

                        }

                    if (my_order_recycler.size() > 0) {

                        packageRecycler = new PackageRecycler(getApplicationContext(), R.layout.development_recycler, my_order_recycler);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Development.this, 1);
                        comp.setLayoutManager(mLayoutManager);
                        comp.setAdapter(packageRecycler);
                        comp.getRecycledViewPool().clear();
                        packageRecycler.notifyItemRangeChanged(0, my_order_recycler.size());

                        //
//


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
    public class PackageRecycler extends RecyclerView.Adapter<PackageRecycler.ViewHolder>{

        Context applicationContext;
        int retailer_weekly_sku_item;
        ArrayList<DevelopmentModel> retailer_weekly_sku_model;

        public PackageRecycler(Context applicationContext, int retailer_weekly_sku_item, ArrayList<DevelopmentModel> retailer_weekly_sku_model) {
            this.applicationContext=applicationContext;
            this.retailer_weekly_sku_model=retailer_weekly_sku_model;
            this.retailer_weekly_sku_item=retailer_weekly_sku_item;
        }

        @Override
        public PackageRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.development_recycler, parent, false);
           PackageRecycler.ViewHolder viewHolder=new PackageRecycler.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PackageRecycler.ViewHolder holder, final int position) {

            // Picasso.with(getApplicationContext()).load("https://app-admin.kwalityhosting.com/package_image/original/"+retailer_weekly_sku_model.get(position).getP_cover_photo()).into(holder.imh);

                holder.subarename.setText(retailer_weekly_sku_model.get(position).getArea());
                holder.subject_tv.setText(retailer_weekly_sku_model.get(position).getSubject());

                holder.areaname.setText(retailer_weekly_sku_model.get(position).getFinancial_year());

                // holder.texttt.setText(retailer_weekly_sku_model.get(position).getCom_text());
                holder.values.setText(retailer_weekly_sku_model.get(position).getNidhi());
            if(retailer_weekly_sku_model.get(position).getStatus().equals("0")){
                holder.valuesdate.setText("Work in progress");
                holder.valuesdate.setTextColor(Color.parseColor("#ff0000"));
            }else {

                holder.valuesdate.setText("Completed");
            }

                holder.text_area.setText("Financial Year");
                holder.text_subarea.setText("Area");

                holder.textvalue.setText("Nidhi ");
                holder.textvaluedate.setText("Status");
                holder.text_subject.setText("Subject");

                holder.texttt_detail.setText("Work Description");
                holder.texttt_detail.setVisibility(View.VISIBLE);
                holder.texttt_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Development.this);
                        builder.setCancelable(true);
                        builder.setMessage(retailer_weekly_sku_model.get(position).getWork_description());
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
holder.valuesdateesti.setText(new StringBuilder().append(Html.fromHtml("&#x20B9;")).append(retailer_weekly_sku_model.get(position).getEstimate()));

            if(retailer_weekly_sku_model.get(position).getImage().equals("")){
                holder.texttt.setVisibility(View.INVISIBLE);
            }else {
                holder.texttt.setVisibility(View.VISIBLE);
                holder.texttt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), VideoShow.class);
                        i.putExtra("news",retailer_weekly_sku_model.get(position).getArea()+" - "+retailer_weekly_sku_model.get(position).getFinancial_year());
                        i.putExtra("name",retailer_weekly_sku_model.get(position).getImage());
                        startActivity(i);
                    }
                });
            }








        }
        public  void setFilter(ArrayList<DevelopmentModel> namesget){
            retailer_weekly_sku_model=new ArrayList<>();
            retailer_weekly_sku_model.addAll(namesget);
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return retailer_weekly_sku_model.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subarename,areaname,subcatarename,valuesdateesti;
            TextView texttt_detail,text_area,text_subarea,text_category,text_subcate,textvaluedate;
            TextView texttt,values,textvalue,valuesdate, text_subject, subject_tv;

            CardView crashcourse;
            public ViewHolder(View itemView) {
                super(itemView);
                texttt_detail=itemView.findViewById(R.id.texttt_detail);

                textvaluedate=itemView.findViewById(R.id.textvaluedate);
                valuesdate=itemView.findViewById(R.id.valuesdate);
                textvalue=itemView.findViewById(R.id.textvalue);
                text_subcate=itemView.findViewById(R.id.text_subcate);
                text_category=itemView.findViewById(R.id.text_category);
                text_subarea=itemView.findViewById(R.id.text_subarea);
                text_area=itemView.findViewById(R.id.text_area);
                valuesdateesti=itemView.findViewById(R.id.valuesdateesti);
                values=itemView.findViewById(R.id.values);
                subarename=itemView.findViewById(R.id.subarename);
                areaname=itemView.findViewById(R.id.areaname);
                crashcourse=itemView.findViewById(R.id.crashcourse);
                text_subject=itemView.findViewById(R.id.text_subject);
                subject_tv=itemView.findViewById(R.id.subject_tv);

                subcatarename=itemView.findViewById(R.id.subcatarename);
                texttt=itemView.findViewById(R.id.texttt);


            }
        }
    }













}