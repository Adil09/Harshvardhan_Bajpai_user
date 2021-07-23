package com.harshvardhanbajpai.otherutilsfiles;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.harshvardhanbajpai.R;

public class Const {

    static ProgressDialog progressDialog;
    static Dialog dialog;

    public static String BASEPATH= "http://mla-admin.sitsald.co.in/users/";
    /*public static final String IMAGE_DIRECTORY_NAME = "Venya File Upload";
    public static String imagepath = "imagepath";*/


    public static String prabharilist = BASEPATH+"prabharilist.json";


    /*...
    * Teacher API
    * ...*/
    public  static String placeholder = "https://srmssc.geniuscloudschool.co.in/1/student/placeholder.png";
    public  static String playstoreurl_withoutpackagename = "https://play.google.com/store/apps/details?id=";


    /*...principal api
    * ...*/


    public static void showProgress(Context context){

        dialog = new Dialog(context);
        try {
            {
                //dialog.setContentView(R.layout.showprogress); // showing digify logo
                dialog.setContentView(R.layout.show_progress_circle); // spinner circle

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.getWindow().setDimAmount(0.0f);
            }
        } catch (Exception e) { }
        try {
            dialog.setCancelable(true);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static void dismisProgress(){

        dialog.dismiss();
    }

    public static void showProgresshorizontal(Context context){

        dialog = new Dialog(context);
        try {
            {
                //dialog.setContentView(R.layout.showprogress); //
                dialog.setContentView(R.layout.show_progress_horizontal); // horizontal progress

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.getWindow().setDimAmount(0.0f);
            }
        } catch (Exception e) { }
        try {
            dialog.setCancelable(true);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static void dismisProgresshorizontal(){

        dialog.dismiss();
    }

    public static void showProgresswithback(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public static void dismisProgresswithback(){
        progressDialog.dismiss();
    }

    public static boolean CheckInternetConnection(Context context)
    {
        boolean check ;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if(connectivityManager != null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if(networkInfo != null && networkInfo.isConnected())
            check=true;
        else
            check=false;

        return check;
    }

    public interface IOnBackPressed {
        /**
         * If you return true the back press will not be taken into account, otherwise the activity will act naturally
         * @return true if your processing has priority if not false
         */
        boolean onBackPressed();
    }

}
