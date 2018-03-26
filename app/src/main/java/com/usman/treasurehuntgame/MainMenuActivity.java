package com.usman.treasurehuntgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    CardView library;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setIds();
        setListeners();
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
}
