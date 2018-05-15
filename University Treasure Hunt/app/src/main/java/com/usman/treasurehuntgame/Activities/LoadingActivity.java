package com.usman.treasurehuntgame.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.Firebase.FirebaseHandler;
import com.usman.treasurehuntgame.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import spencerstudios.com.bungeelib.Bungee;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "LoadingActivity";
    private View inflaterView;
    BroadcastReceiver broadCastReceiver;
    Boolean allowed = false;
    Boolean timerFinished = false;
    private ProgressDialog progressDialog;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        askForPermission();


    }

    void startMenuActivity(){
        if(isInternetConnected()){
            new FirebaseHandler(this).checkAndDownloadNewStage(new JsonFileReader(this).getStagesNames());

            Log.d(TAG, "onCreate: ");
            timer = new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish: ");
                    timerFinished =true;
                    if (allowed) {
                        startActivity(new Intent(LoadingActivity.this, MenuActivity.class));
                        Bungee.spin(LoadingActivity.this);
                        finish();
                    }

                }
            }.start();

        }else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LoadingActivity.this, MenuActivity.class));
                    Bungee.spin(LoadingActivity.this);
                    finish();
                }
            }, 5000);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
        unregisterLocalBroadcastReceiver();
    }

    public Boolean isInternetConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    private void registerLocalBroadcastReceiver(){
        Log.d(TAG, "registerLocalBroadcastReceiver: ");
        broadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                Log.d(TAG, "onReceive: ");
                if(FirebaseHandler.newStagesToDownloadHashMap.size() == 0){
                    if(timerFinished) {
                        startActivity(new Intent(LoadingActivity.this, MenuActivity.class));
                        Bungee.spin(LoadingActivity.this);
                        finish();
                    }else {
                        allowed = true;
                    }
                }else {
                    String[] urls = new String[100];
                    for (int i = 0; i < FirebaseHandler.newStagesToDownloadHashMap.size(); i++) {
                        urls[i] = FirebaseHandler.newStagesToDownloadHashMap.get("url").toString();
                    }
                    new DownloadFileFromURL().execute(urls);
                }
            }};
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, new IntentFilter(FirebaseHandler.STAGES_CHECK_COMPLETED_BROADCAST));
    }
    private void unregisterLocalBroadcastReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        Log.d(TAG, "unregisterLocalBroadcastReceiver: ");
    }




    //////
    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        File fileStorage = null;
        File outputFile = null;
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoadingActivity.this);
            progressDialog.setMessage("Downloading new stages");
            progressDialog.show();        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                for (int i = 0; i < f_url.length; i++) {
                    URL url = new URL(f_url[i]);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }
                    //If File is not present create directory
                    File fileStorage = new File(Environment.getExternalStorageDirectory()+ "/", getString(R.string.app_name)+"/stages");
                    if(!fileStorage.exists()) {
                        fileStorage.mkdirs();
                        boolean result = fileStorage.mkdirs();
                        Log.d("MyActivity", "mkdirs: " + result);
                    }

                    outputFile = new File(fileStorage, f_url[i]);//Create Output file in Main File

//                    //Create New File if not present
//                    if (!outputFile.exists()) {
//                        outputFile.createNewFile();
//                        Log.e(TAG, "File Created");
//                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();
                }


            } catch (Exception e) {
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    Toast.makeText(LoadingActivity.this, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }
            Log.d(TAG, "onPostExecute: ");
            if(timerFinished) {
                startActivity(new Intent(LoadingActivity.this, MenuActivity.class));
                Bungee.spin(LoadingActivity.this);
                finish();
            }else {
                allowed = true;
            }
            super.onPostExecute(result);

        }

    }


    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        registerLocalBroadcastReceiver();
                        startMenuActivity();
                    } else {
                        new AlertDialog.Builder(this).setMessage("Application needs to have Write Storage permission to work properly!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        askForPermission();
                                    }
                                })
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finishAffinity();
                                        System.exit(0);
                                    }
                                }).show();
//                        Snackbar.make(this.getCurrentFocus(),"Application needs to have Write Storage permission to work properly!",Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Action", null).show();
                    }
                }

            }
        }
    }
}
