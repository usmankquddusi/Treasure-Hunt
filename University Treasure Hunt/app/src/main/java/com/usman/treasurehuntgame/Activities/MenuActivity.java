package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.R;

import org.json.JSONException;

import spencerstudios.com.bungeelib.Bungee;

public class MenuActivity extends BaseActivity {

    private static final String TAG = "MenuActivity";
    ImageView play_imageview, scoreboard_imageview, settings_imageview;
    TextView scoreTv;
    JsonFileReader jsonFileReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_menu);
        setIds();
        setListeners();
        jsonFileReader = new JsonFileReader(this);
        try {
            scoreTv.setText("Score: "+ jsonFileReader.readPlayerDataFromFile().getString("score"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void setIds(){
        play_imageview = (ImageView) findViewById(R.id.play_game_imageview_menu);
        scoreboard_imageview = (ImageView) findViewById(R.id.score_game_imageview_menu);
        scoreTv = (TextView) findViewById(R.id.score_tv_menu);
    }

    void setListeners(){

        play_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,LevelSelectionActivity.class));
                Bungee.fade(MenuActivity.this);
            }
        });
        scoreboard_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,ScoreBoardActivity.class));
                Bungee.fade(MenuActivity.this);
            }
        });

    }


}
