package com.harshvardhanbajpai;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PhotoGallery extends AppCompatActivity {
    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
   // int[] images = {R.drawable.banner1, R.drawable.banner2,R.drawable.banner3, R.drawable.banner4,R.drawable.banner5, R.drawable.banner6};
ImageView photo_arrow;
ArrayList<GetSetImageGallery>images=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        photo_arrow=findViewById(R.id.photo_arrow);
        photo_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery); // get the reference of Gallery
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView); // get the reference of ImageView
        customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images); // initialize the adapter
        simpleGallery.setAdapter(customGalleryAdapter); // set the adapter
        simpleGallery.setSpacing(2);
        // perform setOnItemClickListener event on the Gallery
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Picasso.with(PhotoGallery.this).load(images.get(position).getImage()).fit().into(selectedImageView);
            }
        });
        setSliderImages();
    }

    private void setSliderImages() {
        images.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://mla-admin.sitsald.co.in/users/gallery.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jd = new JSONObject(response);
                    JSONObject jsonObject=jd.getJSONObject("message");

                        JSONArray user = jsonObject.getJSONArray("gallery");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject loopdata = user.getJSONObject(i);
                            JSONObject packagedata = loopdata.getJSONObject("Gallery");
                            images.add(new GetSetImageGallery(packagedata.getString("id"), "http://mla-admin.sitsald.co.in/gallery/" + packagedata.getString("g_name")));
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (images.size() > 0) {
                    customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), images); // initialize the adapter
                    simpleGallery.setAdapter(customGalleryAdapter);

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

}
