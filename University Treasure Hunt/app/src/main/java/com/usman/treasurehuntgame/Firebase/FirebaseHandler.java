package com.usman.treasurehuntgame.Firebase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.usman.treasurehuntgame.Classes.DownloadTask;
import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.Models.ScoreObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class FirebaseHandler {

    private static final String TAG = "FirebaseHandler";
    public static final String SCORE_BROADCAST = "score_broadcast";
    public static final String STAGES_CHECK_COMPLETED_BROADCAST = "stages_broadcast";
    Context context;
    DatabaseReference dbRef;
    JsonFileReader jsonFileReader;
    public static HashMap scoreboardHashmap = new HashMap();
    public static ArrayList<ScoreObj> scoreList = new ArrayList();
    public static HashMap newStagesToDownloadHashMap = new HashMap();

    public FirebaseHandler(Context context) {
        this.context = context;
        dbRef = FirebaseDatabase.getInstance().getReference();
        jsonFileReader =  new JsonFileReader(context);
    }

    public void savePlayerData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String name= null, score=null, age=null;
        JSONObject playerJson=null;
        try {
            playerJson = jsonFileReader.readPlayerDataFromFile();
            name =  playerJson.getString("name");
            age =  playerJson.getString("age");
            score = playerJson.getString("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("name",name);
        hashMap.put("age", age);
        hashMap.put("score", score);
        databaseRef.child("users").child(name).setValue(hashMap);

    }

    public void getScoreBoardData(){
        Log.d(TAG, "getScoreBoardData: ");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreList.clear();
                HashMap data = (HashMap) dataSnapshot.getValue();
                Log.d(TAG, "onDataChange: data:"+data);
                if(data == null) return;
                Set<String> keys = data.keySet();
                String name = null, age= null;
                int score = 0;
                for (String key : keys) {
                    if (data != null) {
                        scoreboardHashmap = (HashMap) data.get(key);
                        Set<String> attrKeys = scoreboardHashmap.keySet();
                        for (String attrkey : attrKeys) {

                            String attrData = scoreboardHashmap.get(attrkey).toString();
                            if (attrkey.equals("name")) {
                                name = attrData;
                            }
                            if (attrkey.equals("age")) {
                                age = attrData;
                            }
                            if (attrkey.equals("score")) {
                                score = Integer.parseInt(attrData);
                            }

                        }
                        Log.d(TAG, "onDataChange: name:" + name + "|age:" + age + "|score:" + score);
                        scoreList.add(new ScoreObj(name, age, score));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SCORE_BROADCAST));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkAndDownloadNewStage(final ArrayList<String> current_stage_names){
        Log.d(TAG, "checkAndDownloadNewStage: ");
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("stages");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("stages");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap data = (HashMap) dataSnapshot.getValue();
                Log.d(TAG, "onDataChange: data:"+data);
                if(data == null) return;
                Set<String> keys = data.keySet();
                String name = null, url= null;
                for (String key : keys) {
                    // locally stored stages does not contain the name of stage name from firebase then download that stage
                        if(!current_stage_names.contains(key+".json")){
                            if (data != null) {
                                newStagesToDownloadHashMap = (HashMap) data.get(key);
                                Set<String> attrKeys = newStagesToDownloadHashMap.keySet();
                                for (String attrkey : attrKeys) {

                                    String attrData = newStagesToDownloadHashMap.get(attrkey).toString();
                                    if (attrkey.equals("name")) {
                                        name = attrData;
                                    }
                                    if (attrkey.equals("url")) {
                                        url = attrData;
                                    }


                                }
                                new DownloadTask(context,name,url);
                                Log.d(TAG, "onDataChange: new stage downloading name:" + name + "|url:" +url);
                            }

                    }

                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(STAGES_CHECK_COMPLETED_BROADCAST));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void updateScore(String username, final int score){
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("score");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "updateScore onDataChange: ");
                if(dataSnapshot.getValue() == null) {
                    dbRef.setValue(null);
                }else {
                    dbRef.setValue(score);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


}
