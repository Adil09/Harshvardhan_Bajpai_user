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

public class QuestionConfi {
    private static String res;

    private static Context context;
    public static  void StrictPolicy()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //    d_id, col_name(police_verification, photo, id_proof, address_proof, driving_license), image
    public static String uploadImage(final String img,final String pat,String area, String subarea,
                                     String subcat, String userid, String comtext,String url,String normal) {
        Log.d("all_data", "" + "\n" + comtext + "\n"  + "\n"  );
        String res = null;
        try {
            StrictPolicy();
            File sourceFile = new File(pat);
File sourceFile1=new File(img);
//            FileBody pic = new FileBody(new File(sourceImageFile));
            if (sourceFile.exists()) {

//                DecodeFile(sourceFile);
            } else {

            }

            final MediaType MEDIA_TYPE_VDO = MediaType.parse("video/*");
            String filename = pat.substring(pat.lastIndexOf("/") + 1);
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String filename1 = img.substring(img.lastIndexOf("/") + 1);

            RequestBody requestBody;
            requestBody = new MultipartBody.Builder()

                    .addFormDataPart("video", "")

                    .addFormDataPart("image", "")
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("area_id", area)
                    .addFormDataPart("area_sub_id", subarea)
                    .addFormDataPart("com_title", subcat)
                    .addFormDataPart("user_id",userid)
                    .addFormDataPart("com_text",comtext)
                    .addFormDataPart("com_type","QA")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
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
