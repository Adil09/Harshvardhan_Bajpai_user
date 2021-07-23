package com.harshvardhanbajpai;


import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUploader {
    private static String res;

    private static Context context;
    public static  void StrictPolicy()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //    d_id, col_name(police_verification, photo, id_proof, address_proof, driving_license), image
    public static String uploadImage(final String img,String area) {

        String res = null;
        try {
            StrictPolicy();

File sourceFile1=new File(img);
//            FileBody pic = new FileBody(new File(sourceImageFile));
            if (sourceFile1.exists()) {
                Log.d("img", "present");
//                DecodeFile(sourceFile);
            } else {
                Log.d("img", "Not present");
            }

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String filename1 = img.substring(img.lastIndexOf("/") + 1);

            RequestBody requestBody;
            requestBody = new MultipartBody.Builder()

                    .addFormDataPart("profile_photo", filename1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1)).setType(MultipartBody.FORM)
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", area)

                    .build();

            Request request = new Request.Builder()
                    .url("http://mla-admin.sitsald.co.in/users/photo_edit.json")
                    .post(requestBody)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            res = response.body().string();
            return res;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return res;
    }



}
