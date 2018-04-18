package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import spencerstudios.com.bungeelib.Bungee;

/**
 * Created by sibghat on 4/15/2018.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Class activity){
        startActivity(new Intent(BaseActivity.this, activity));
        Bungee.shrink(BaseActivity.this);
    }
}
