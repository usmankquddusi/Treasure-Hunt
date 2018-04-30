package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.usman.treasurehuntgame.Classes.JsonFileWriter;
import com.usman.treasurehuntgame.R;

import java.util.Random;

import spencerstudios.com.bungeelib.Bungee;

public class CreateNewPlayerActivity extends BaseActivity {

    private static final String TAG = "CreateNewPlayerActivity";
    Button createPlayerBtn;
    EditText nametxt;
    JsonFileWriter jsonFileWriter;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_player);
        setIds();
        setListeners();
    }
    void setIds(){
        createPlayerBtn = (Button) findViewById(R.id.create_player_btn);
        nametxt = (EditText) findViewById(R.id.enter_name_edittext);
//        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view_done);
    }

    void setListeners(){
        createPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nametxt.getText().toString();
                int min = 1;
                int max = 10000;

                Random r = new Random();
                int randomNumber = r.nextInt(max - min + 1) + min;

                if(name.equals("")){
                    Toast.makeText(getBaseContext(),"Enter your name!",Toast.LENGTH_SHORT).show();
                }else {
                    jsonFileWriter = new JsonFileWriter(CreateNewPlayerActivity.this);
                    if(jsonFileWriter.writePlayerObjectFile(name+randomNumber)){
                        startActivity(new Intent(CreateNewPlayerActivity.this,LevelSelectionActivity.class));
                        Bungee.fade(CreateNewPlayerActivity.this);
//                        lottieAnimationView.playAnimation();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        },3000);
                    }
                }
            }
        });

//        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
    }


}
