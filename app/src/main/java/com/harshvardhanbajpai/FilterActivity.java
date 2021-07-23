package com.harshvardhanbajpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {
TextView nidhii,areaaa,yearr;
RecyclerView area_recycler,year_recycler,nidhi_recycler;
ArrayList<FilterYear>filterYears=new ArrayList<>();
    ArrayList<FilterArea>filterArea=new ArrayList<>();
    ArrayList<FilterNidhi>filterNidhi=new ArrayList<>();

TextView textcl;
String filteryearr="n";
String filterarea="n";
    String filterni="n";
String claer="1";
TextView text;
LinearLayout applyfilter;
TextView toolbar_title_back;
String memberf,membera,membern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        toolbar_title_back = findViewById(R.id.toolbar_title_back);
        toolbar_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences s = getSharedPreferences("Filter_id", MODE_PRIVATE);
        memberf = s.getString("f_id", "N/A");
        membera = s.getString("a_id", "N/A");
        membern = s.getString("n_id", "N/A");
        text=findViewById(R.id.text);
        nidhii=findViewById(R.id.nidhii)
 ;
        yearr=findViewById(R.id.yearr);
        areaaa=findViewById(R.id.areaaa);
        textcl=findViewById(R.id.textcl);
        year_recycler=findViewById(R.id.year_recycler);
        area_recycler=findViewById(R.id.area_recycler);
        nidhi_recycler=findViewById(R.id.nidhi_recycler);
        areaaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claer="2";
year_recycler.setVisibility(View.GONE);
nidhi_recycler.setVisibility(View.GONE);
area_recycler.setVisibility(View.VISIBLE);
                yearr.setBackgroundColor(Color.parseColor("#D4D4D4"));
                areaaa.setBackgroundColor(Color.parseColor("#F48120"));
                nidhii.setBackgroundColor(Color.parseColor("#D4D4D4"));


            }
        });

        yearr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claer="1";
                year_recycler.setVisibility(View.VISIBLE);
                nidhi_recycler.setVisibility(View.GONE);
                area_recycler.setVisibility(View.GONE);
                yearr.setBackgroundColor(Color.parseColor("#F48120"));
                areaaa.setBackgroundColor(Color.parseColor("#D4D4D4"));
                nidhii.setBackgroundColor(Color.parseColor("#D4D4D4"));


            }
        });
        nidhii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claer="3";
                yearr.setBackgroundColor(Color.parseColor("#D4D4D4"));
                areaaa.setBackgroundColor(Color.parseColor("#D4D4D4"));
                nidhii.setBackgroundColor(Color.parseColor("#F48120"));


                year_recycler.setVisibility(View.GONE);
                nidhi_recycler.setVisibility(View.VISIBLE);
                area_recycler.setVisibility(View.GONE);



            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                package_list();
                package_area();
                package_nidhi();



                filteryearr="n";

                filterarea="n";

                filterni="n";
                memberf="N/A";
                membera="N/A";
                membern="N/A";
            }
        });

        textcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(claer.equals("1")){
                   package_list();
                   filteryearr="n";
                   memberf="N/A";
                }else if(claer.equals("2")){
                   package_area();
                   filterarea="n";
                   membera="N/A";
               }else if(claer.equals("3")){
                   package_nidhi();
                   filterni="n";
                   membern="N/A";
               }
            }
        });

        package_list();
        package_area();
        package_nidhi();
        applyfilter=findViewById(R.id.applyfilter);
        applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Year_id", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("year_id", filteryearr).commit();

                SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences("Area_id", MODE_PRIVATE);
                SharedPreferences.Editor edito = sharedPreference.edit();
                edito.putString("area_id", filterarea).commit();

                SharedPreferences sharedPreferencen = getApplicationContext().getSharedPreferences("Nidhi_id", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferencen.edit();
                edit.putString("n_id", filterni).commit();

                SharedPreferences sharedPreferenc = getApplicationContext().getSharedPreferences("Call_id", MODE_PRIVATE);
                SharedPreferences.Editor edi = sharedPreferenc.edit();
                edi.putString("cn_id", "2").commit();




            startActivity(new Intent(getApplicationContext(),Development.class));

            }
        });

    }

//area

    private void package_area() {

        filterArea.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_saerch_area.json", new Response.Listener<String>() {
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



                    if (filterArea.size() > 0) {

                        RecyclerviewAddressAdapterArea packageRecycler = new RecyclerviewAddressAdapterArea(getApplicationContext(),filterArea);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                        area_recycler.setLayoutManager(mLayoutManager);
                        area_recycler.setAdapter(packageRecycler);



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

    public class RecyclerviewAddressAdapterArea extends RecyclerView.Adapter<RecyclerviewAddressAdapterArea.DataHolder> {
        ArrayList<FilterArea> getSetAddressListArrayList;
        Context context;
        private int lastSelectedPosition = -1;
        public RecyclerviewAddressAdapterArea(Context context, ArrayList<FilterArea> getSetAddressLists) {
            this.context = context;
            this.getSetAddressListArrayList = getSetAddressLists;
        }

        @NonNull
        @Override
        public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.item_area, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DataHolder dataHolder, final int i) {

            dataHolder. txt_house_street_address.setText(getSetAddressListArrayList.get(i).getYear());
          dataHolder. txt_house_street_address.setChecked(dataHolder.getAdapterPosition()==lastSelectedPosition);
            dataHolder.txt_house_street_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastSelectedPosition = dataHolder.getAdapterPosition();
                    notifyDataSetChanged();

                  filterarea=getSetAddressListArrayList.get(i).getYear();

                }
            });
            if(membera.equals("N/A")){

            }else {
                if (getSetAddressListArrayList.get(i).getYear().equals(membera)) {
                    dataHolder.txt_house_street_address.setChecked(true);
                    filterarea=getSetAddressListArrayList.get(i).getYear();
                }
            }
        }

        @Override
        public int getItemCount() {
            return getSetAddressListArrayList.size();
        }

        public class DataHolder extends RecyclerView.ViewHolder {
            RadioButton txt_house_street_address;


            public DataHolder(@NonNull View itemView) {
                super(itemView);

                txt_house_street_address =  (RadioButton)itemView.findViewById(R.id.txt_house_street_addres);


//
            }
        }


    }

    //
    private void package_list() {

        filterYears.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://mla-admin.sitsald.co.in/users/app_financial_year.json", new Response.Listener<String>() {
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



                    if (filterYears.size() > 0) {

                        RecyclerviewAddressAdapter packageRecycler = new RecyclerviewAddressAdapter(getApplicationContext(),filterYears);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                        year_recycler.setLayoutManager(mLayoutManager);
                        year_recycler.setAdapter(packageRecycler);
                        year_recycler.getRecycledViewPool().clear();


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

    public class RecyclerviewAddressAdapter extends RecyclerView.Adapter<RecyclerviewAddressAdapter.DataHolder> {
        ArrayList<FilterYear> getSetAddressListArrayList;
        Context context;
        private int lastSelectedPosition = -1;
        public RecyclerviewAddressAdapter(Context context, ArrayList<FilterYear> getSetAddressLists) {
            this.context = context;
            this.getSetAddressListArrayList = getSetAddressLists;
        }

        @NonNull
        @Override
        public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.item_year, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DataHolder dataHolder, final int i) {



            dataHolder. txt_house_street_address.setText(""+getSetAddressListArrayList.get(i).getYear());
            dataHolder. txt_house_street_address.setChecked(dataHolder.getAdapterPosition()==lastSelectedPosition);
dataHolder.txt_house_street_address.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        lastSelectedPosition = dataHolder.getAdapterPosition();
        notifyDataSetChanged();

        filteryearr=getSetAddressListArrayList.get(i).getYear();

    }
});
            if(memberf.equals("N/A")){

            }else {
                if (getSetAddressListArrayList.get(i).getYear().equals(memberf)) {
                    dataHolder.txt_house_street_address.setChecked(true);
                    filteryearr=getSetAddressListArrayList.get(i).getYear();
                }
            }
        }

        @Override
        public int getItemCount() {
            return getSetAddressListArrayList.size();
        }

        public class DataHolder extends RecyclerView.ViewHolder {
            RadioButton txt_house_street_address;


            public DataHolder(@NonNull View itemView) {
                super(itemView);

                txt_house_street_address =  (RadioButton)itemView.findViewById(R.id.txt_house_street_addres);


//
            }
        }


    }



//
private void package_nidhi() {

    filterNidhi.clear();

    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/app_nidhi.json", new Response.Listener<String>() {
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



                if (filterNidhi.size() > 0) {

                    RecyclerviewAddressAdapter2 packageRecycler = new RecyclerviewAddressAdapter2(getApplicationContext(),filterNidhi);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(FilterActivity.this, 1);
                    nidhi_recycler.setLayoutManager(mLayoutManager);
                    nidhi_recycler.setAdapter(packageRecycler);
                    nidhi_recycler.getRecycledViewPool().clear();


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

    public class RecyclerviewAddressAdapter2 extends RecyclerView.Adapter<RecyclerviewAddressAdapter2.DataHolder> {
        ArrayList<FilterNidhi> getSetAddressListArrayList;
        Context context;
        private int lastSelectedPosition = -1;
        public RecyclerviewAddressAdapter2(Context context, ArrayList<FilterNidhi> getSetAddressLists) {
            this.context = context;
            this.getSetAddressListArrayList = getSetAddressLists;
        }

        @NonNull
        @Override
        public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.item_year, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DataHolder dataHolder, final int i) {



            dataHolder. txt_house_street_address.setText(""+getSetAddressListArrayList.get(i).getYear());
            dataHolder. txt_house_street_address.setChecked(dataHolder.getAdapterPosition()==lastSelectedPosition);
            dataHolder.txt_house_street_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = dataHolder.getAdapterPosition();
                    notifyDataSetChanged();

                    filterni=getSetAddressListArrayList.get(i).getYear();

                }
            });
            if(membern.equals("N/A")){

            }else {
                if (getSetAddressListArrayList.get(i).getYear().equals(membern)) {
                    dataHolder.txt_house_street_address.setChecked(true);
                    filterni=getSetAddressListArrayList.get(i).getYear();
                }
            }
        }

        @Override
        public int getItemCount() {
            return getSetAddressListArrayList.size();
        }

        public class DataHolder extends RecyclerView.ViewHolder {
            RadioButton txt_house_street_address;


            public DataHolder(@NonNull View itemView) {
                super(itemView);

                txt_house_street_address =  (RadioButton)itemView.findViewById(R.id.txt_house_street_addres);


//
            }
        }


    }



    //
}