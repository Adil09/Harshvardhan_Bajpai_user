package com.harshvardhanbajpai;


import android.content.Context;
import android.net.Uri;
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

import static io.github.memfis19.annca.internal.utils.Utils.getMimeType;

public class ImageCom {
    private static String res;

    private static Context context;
    public static  void StrictPolicy()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //    d_id, col_name(police_verification, photo, id_proof, address_proof, driving_license), image
    public static String uploadImage(final String pat,String area, String subarea, String cat ,
                                     String subcat, String userid, String comtext,String url) {
        Log.d("all_data", "" + "\n" + comtext + "\n"  + "\n"  );
        String res = null;
        try {
            StrictPolicy();
            File sourceFile = new File(pat);

//            FileBody pic = new FileBody(new File(sourceImageFile));
            if (sourceFile.exists()) {
                Log.d("img", "present");
//                DecodeFile(sourceFile);
            } else {
                Log.d("img", "Not present");
            }
            Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());


            final MediaType MEDIA_TYPE_VDO = MediaType.parse("video/*");
            String filename = pat.substring(pat.lastIndexOf("/") + 1);


            RequestBody requestBody;
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)

                    .addFormDataPart("video", filename, RequestBody.create(MEDIA_TYPE_VDO, sourceFile)).setType(MultipartBody.FORM)
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("area_id", area)
                    .addFormDataPart("area_sub_id", subarea)
                    .addFormDataPart("cat_id", cat)
                    .addFormDataPart("cat_sub_id", subcat)
                    .addFormDataPart("user_id",userid)
                    .addFormDataPart("com_text",comtext)
                    .addFormDataPart("com_type","confidential")
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
