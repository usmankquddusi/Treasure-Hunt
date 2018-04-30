package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.usman.treasurehuntgame.R;

import spencerstudios.com.bungeelib.Bungee;

public class MenuActivity extends BaseActivity {

    private static final String TAG = "MenuActivity";
    ImageView play_imageview, scoreboard_imageview, settings_imageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setIds();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void setIds(){
        play_imageview = (ImageView) findViewById(R.id.play_game_imageview_menu);
        scoreboard_imageview = (ImageView) findViewById(R.id.score_game_imageview_menu);
        settings_imageview = (ImageView) findViewById(R.id.settings_game_imageview_menu);
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
        settings_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
