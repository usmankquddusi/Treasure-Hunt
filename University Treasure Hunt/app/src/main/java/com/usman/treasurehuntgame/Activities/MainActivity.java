package com.usman.treasurehuntgame.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.Classes.JsonFileWriter;
import com.usman.treasurehuntgame.Classes.StaticVariables;
import com.usman.treasurehuntgame.Classes.TypeWriter;
import com.usman.treasurehuntgame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    JsonFileReader jsonFileReader;
    JsonFileWriter jsonFileWriter;
    TextView scoreTv, hintTv;
    TypeWriter questionTypeWriter;
    EditText answerField;
    Button submitBtn;
    int position;
    String question="", answer=null, hint=null;
    int currentSavedProgress = 0;
    JSONArray questionListJsonObject;
    private  int questionCounter=0;
    String currentStageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIds();
        setListeners();
        jsonFileReader = new JsonFileReader(this);
        jsonFileWriter = new JsonFileWriter(this);

        position = getIntent().getExtras().getInt("position");
        Log.d(TAG, "onCreate: position:"+position);

        currentStageName = jsonFileReader.getStagesNames().get(position);
        getSavedPlayerProgress();
        getQuestionsListJson();
        getCurrentQuestionAnswer();
        setScore();
        setQuestionInTypeWriter();
    }

    void setIds(){
        scoreTv = (TextView) findViewById(R.id.score_tv);
        questionTypeWriter = (TypeWriter) findViewById(R.id.question_tv);
        hintTv = (TextView) findViewById(R.id.hint_tv);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        answerField = (EditText) findViewById(R.id.answer_edittext);
    }

    void setListeners(){
        hintTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Hint");
                builder1.setMessage(hint);
                builder1.setCancelable(true);

                builder1.setNeutralButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchAnswer(answerField.getText().toString());

            }
        });
    }

    void getSavedPlayerProgress() {

        try {
            if(jsonFileReader.readPlayerDataFromFile().getString(currentStageName) != null) {
                currentSavedProgress = Integer.parseInt(jsonFileReader.readPlayerDataFromFile().getString(currentStageName));
                Log.d(TAG, "getSavedPlayerProgress: currentSavedProgress:" + currentSavedProgress);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void savePlayerProgress() {
        JSONObject playerJsonObject = null;

        try {
            playerJsonObject = jsonFileReader.readPlayerDataFromFile();

            playerJsonObject.put(currentStageName, currentSavedProgress);
            playerJsonObject.put("score", StaticVariables.myScore);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            String json = null;

        json = playerJsonObject.toString();
        jsonFileWriter.writePlayerObjectFile(json);

    }

    void getQuestionsListJson(){
        try {
            JSONObject stageJsonObject = jsonFileReader.readStageDataFromFile(currentStageName);
            Log.d(TAG, "getQuestionsListJson: stageJsonObject:"+stageJsonObject);
            questionListJsonObject = stageJsonObject.getJSONArray("questions_list");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getCurrentQuestionAnswer() {
        Log.d(TAG, "getCurrentQuestionAnswer: questionListJsonObject.length():"+questionListJsonObject.length());
        if(currentSavedProgress < questionListJsonObject.length()) {
            JSONObject object = null; // This is q1, q2 according to counter
            try {
                object = questionListJsonObject.getJSONObject(currentSavedProgress);
                Log.d(TAG, "getCurrentQuestionAnswer: object"+object);
                question = object.getString("question");
                answer = object.getString("answer");
                hint = object.getString("hint");

                Log.d(TAG, "getCurrentQuestionAnswer: question:"+question);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //Stage Completed
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Congratulations. You have successfully completed this stage.");
            builder1.setCancelable(true);

            builder1.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            startActivity(new Intent(MainActivity.this,LevelSelectionActivity.class));
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }
    }

    boolean matchAnswer(String answer_from_field){
        Log.d(TAG, "matchAnswer: ");
        if(answer.equalsIgnoreCase(answer_from_field)){
            currentSavedProgress++;
            StaticVariables.myScore ++;
            savePlayerProgress();

            if(StaticVariables.isInternetConnected)
                firebaseHandle.updateScore(StaticVariables.myName,StaticVariables.myScore);

            Toast.makeText(MainActivity.this,"Correct Answer",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setScore();
                    getCurrentQuestionAnswer();
                    answerField.setText("");
                    if(currentSavedProgress< questionListJsonObject.length()) {
                        setQuestionInTypeWriter();

                    }
                }
            },2000);
            return true;
        }else{
            Toast.makeText(MainActivity.this,"Wrong Answer",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    
    void setQuestionInTypeWriter(){
        Log.d(TAG, "setQuestionInTypeWriter: question:"+question);
        questionTypeWriter.setText("");
        questionTypeWriter.setCharacterDelay(100);
        questionTypeWriter.animateText(question);
    }

    void setScore(){
        Log.d(TAG, "setScore: ");
        scoreTv.setText(currentSavedProgress+"");
    }

}
