package com.usman.treasurehuntgame.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.R;

import org.json.JSONException;

public class MainMenuActivity extends AppCompatActivity {

    private static String TAG = "MainMenuActivity";
    CardView library;
    JsonFileReader jsonFileReader = new JsonFileReader(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main_menu);
        setIds();
        setListeners();
        askForPermission();
        getStages();

    }
    void checkPlayerDataPresent(){
        if(!jsonFileReader.isFilePresent(this,getString(R.string.player_data))){
            startActivity(new Intent(MainMenuActivity.this, CreateNewPlayerActivity.class));
        }else{
            try {
                Log.d(TAG, "checkPlayerDataPresent: reading player data from file");
                jsonFileReader.readPlayerDataFromFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setIds(){
        library = (CardView) findViewById(R.id.library_id);
    }

    void setListeners(){
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
            }
        });
    }

    void getStages(){
        if(jsonFileReader.isStagesPresent(getString(R.string.stages_folder))){
            try {
                jsonFileReader.readStages();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                        checkPlayerDataPresent();
                    } else {
                        Snackbar.make(this.getCurrentFocus(),"Application needs to have Write Storage permission to work properly!",Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();
                    }
                }

            }
        }
    }
}