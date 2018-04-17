package com.usman.treasurehuntgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.usman.treasurehuntgame.Classes.JsonFileWriter;
import com.usman.treasurehuntgame.R;

public class CreateNewPlayerActivity extends AppCompatActivity {

    private static final String TAG = "CreateNewPlayerActivity";
    Button createPlayerBtn;
    EditText nametxt;
    JsonFileWriter jsonFileWriter;
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
    }

    void setListeners(){
        createPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nametxt.getText().toString();

                if(name.equals("")){
                    Toast.makeText(getBaseContext(),"Enter your name!",Toast.LENGTH_SHORT).show();
                }else {
                    jsonFileWriter = new JsonFileWriter(CreateNewPlayerActivity.this);
                    if(jsonFileWriter.writePlayerObjectFile(nametxt.getText().toString())){
                        startActivity(new Intent(CreateNewPlayerActivity.this,MainMenuActivity.class));
                    }
                }
            }
        });
    }
}
