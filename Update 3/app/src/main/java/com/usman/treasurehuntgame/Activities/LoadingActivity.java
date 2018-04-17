package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.usman.treasurehuntgame.R;

public class LoadingActivity extends AppCompatActivity {

    private View inflaterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, MainMenuActivity.class));
                finish();
            }
        },3000);
    }
}
